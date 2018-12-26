package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator
import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.core.exceptions.FakeOnlinePaymentNotificationException
import com.thebund1st.tiantong.core.exceptions.OnlinePaymentAlreadyClosedException
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.LocalDateTime

import static com.thebund1st.tiantong.commands.MakeOnlinePaymentCommandFixture.aMakeOnlinePaymentCommand
import static com.thebund1st.tiantong.core.OnlinePayment.Status.*
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.events.OnlinePaymentNotificationEventFixture.anOnlinePaymentNotificationEvent

class OnlinePaymentCommandHandlerTest extends Specification {

    private OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator = Mock()
    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private Clock clock = Mock()
    private OnlinePaymentCommandHandler target = new OnlinePaymentCommandHandler(onlinePaymentIdentifierGenerator,
            onlinePaymentRepository, clock)

    def "it should create an online payment"() {
        given:
        def command = aMakeOnlinePaymentCommand().build()
        def now = LocalDateTime.now()
        def onlinePaymentId = OnlinePayment.Identifier.of("1")

        and:
        clock.now() >> now
        onlinePaymentIdentifierGenerator.nextIdentifier() >> onlinePaymentId

        when:
        def actual = target.handle(command)

        then:
        1 * onlinePaymentRepository.save(_ as OnlinePayment)

        and:
        assert actual.id == onlinePaymentId
        assert actual.amount == command.amount
        assert actual.createdAt == now
        assert actual.lastModifiedAt == now
        assert actual.status == PENDING
        assert actual.method == OnlinePayment.Method.of(command.method)
        assert actual.correlation == command.correlation
    }

    def "it should mark the online payment success and emit payment succeed event"() {
        given:
        def op = anOnlinePayment().build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotificationEvent().succeed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.on(event)

        then:
        assert op.lastModifiedAt == now
        assert op.status == SUCCESS
        assert op.notifiedBy == event.eventId
    }

    def "it should throw when handling duplicate events"() {
        given:
        def op = anOnlinePayment().idIs("154").succeeded().build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotificationEvent().succeed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.on(event)

        then:
        def thrown = thrown(OnlinePaymentAlreadyClosedException)
        assert thrown.getMessage().contains("Online Payment [154] failed to handle [${event}] as it has been marked as SUCCESS")
    }

    def "it should throw when handling fake events"() {
        given:
        def op = anOnlinePayment().idIs("154").amountIs(100).build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotificationEvent().amountIs(150).succeed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.on(event)

        then:
        def thrown = thrown(FakeOnlinePaymentNotificationException)
        assert thrown.getMessage().contains("Online Payment [154][100.0] failed to handle [${event}] due to mismatch amount")
    }

    def "it should mark the online payment failure and emit payment failed event"() {
        given:
        def op = anOnlinePayment().build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotificationEvent().failed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.on(event)

        then:
        assert op.lastModifiedAt == now
        assert op.status == FAILURE
        assert op.notifiedBy == event.eventId
    }

    def "it should mark the online payment failure given duplicate events"() {
        given:
        def op = anOnlinePayment().idIs("154").failed().build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotificationEvent().failed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.on(event)

        then:
        def thrown = thrown(OnlinePaymentAlreadyClosedException)
        assert thrown.getMessage().contains("Online Payment [154] failed to handle [${event}] as it has been marked as FAILURE")
    }


}
