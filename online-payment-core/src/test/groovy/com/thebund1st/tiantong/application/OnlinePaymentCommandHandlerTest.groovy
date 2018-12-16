package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.ZonedDateTime

import static com.thebund1st.tiantong.commands.MakeOnlinePaymentCommandFixture.aMakeOnlinePaymentCommand
import static com.thebund1st.tiantong.core.OnlinePayment.Status.PENDING
import static com.thebund1st.tiantong.core.OnlinePayment.Status.SUCCESS
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.events.OnlinePaymentNotificationEventFixture.anOnlinePaymentNotificationEvent

class OnlinePaymentCommandHandlerTest extends Specification {

    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private Clock clock = Mock()
    private OnlinePaymentCommandHandler target = new OnlinePaymentCommandHandler(onlinePaymentRepository, clock)

    def "it should create an online payment"() {
        given:
        def command = aMakeOnlinePaymentCommand().build()
        def now = ZonedDateTime.now()
        def onlinePaymentId = OnlinePayment.Identifier.of("1")

        and:
        clock.now() >> now
        onlinePaymentRepository.nextIdentifier() >> onlinePaymentId

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
    }

    def "it should mark the online payment success and emit payment succeed event"() {
        given:
        def op = anOnlinePayment().build()
        def now = ZonedDateTime.now()
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


}
