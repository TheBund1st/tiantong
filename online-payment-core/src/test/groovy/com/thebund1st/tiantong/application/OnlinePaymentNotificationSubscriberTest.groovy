package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.core.EventPublisher
import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.core.exceptions.FakeOnlinePaymentNotificationException
import com.thebund1st.tiantong.core.exceptions.OnlinePaymentAlreadyClosedException
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.LocalDateTime

import static com.thebund1st.tiantong.commands.OnlinePaymentNotificationFixture.anOnlinePaymentNotification
import static com.thebund1st.tiantong.core.OnlinePayment.Status.FAILURE
import static com.thebund1st.tiantong.core.OnlinePayment.Status.SUCCESS
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlinePaymentNotificationSubscriberTest extends Specification {

    private EventPublisher eventPublisher = Mock()
    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private Clock clock = Mock()
    private OnlinePaymentNotificationSubscriber target = new OnlinePaymentNotificationSubscriber(onlinePaymentRepository,
            eventPublisher, clock)


    def "it should mark the online payment success and emit payment succeed event"() {
        given:
        def op = anOnlinePayment().build()
        def now = LocalDateTime.now()
        def notification = anOnlinePaymentNotification().succeed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.handle(notification)

        then:
        assert op.lastModifiedAt == now
        assert op.status == SUCCESS
        assert op.notifiedBy == notification.eventId

        and:
        1 * eventPublisher.publish({
            it.onlinePaymentId == op.id
            it.onlinePaymentVersion == op.version
            it.correlation == op.correlation
            it.amount == op.amount
            it.when == now
        })
    }

    def "it should throw when handling duplicate events"() {
        given:
        def op = anOnlinePayment().idIs("154").succeeded().build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotification().succeed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.handle(event)

        then:
        def thrown = thrown(OnlinePaymentAlreadyClosedException)
        assert thrown.getMessage().contains("Online Payment [154] failed to handle [${event}] as it has been marked as SUCCESS")
    }

    def "it should throw when handling fake events"() {
        given:
        def op = anOnlinePayment().idIs("154").amountIs(100).build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotification().amountIs(150).succeed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.handle(event)

        then:
        def thrown = thrown(FakeOnlinePaymentNotificationException)
        assert thrown.getMessage().contains("Online Payment [154][100.0] failed to handle [${event}] due to mismatch amount")
    }

    def "it should mark the online payment failure and emit payment failed event"() {
        given:
        def op = anOnlinePayment().build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotification().failed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.handle(event)

        then:
        assert op.lastModifiedAt == now
        assert op.status == FAILURE
        assert op.notifiedBy == event.eventId

        and:
        1 * eventPublisher.publish({
            it.onlinePaymentId == op.id
            it.onlinePaymentVersion == op.version
            it.correlation == op.correlation
            it.amount == op.amount
            it.when == now
        })
    }

    def "it should mark the online payment failure given duplicate events"() {
        given:
        def op = anOnlinePayment().idIs("154").failed().build()
        def now = LocalDateTime.now()
        def event = anOnlinePaymentNotification().failed().sendTo(op).build()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now

        when:
        //noinspection GroovyAssignabilityCheck
        target.handle(event)

        then:
        def thrown = thrown(OnlinePaymentAlreadyClosedException)
        assert thrown.getMessage().contains("Online Payment [154] failed to handle [${event}] as it has been marked as FAILURE")
    }


}
