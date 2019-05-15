package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.core.*
import com.thebund1st.tiantong.core.exceptions.FakeOnlinePaymentNotificationException
import com.thebund1st.tiantong.core.exceptions.OnlinePaymentAlreadyClosedException
import com.thebund1st.tiantong.events.OnlinePaymentSucceededEvent
import com.thebund1st.tiantong.time.Clock
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDateTime

import static com.thebund1st.tiantong.commands.OnlinePaymentNotificationFixture.anOnlinePaymentNotification
import static com.thebund1st.tiantong.core.OnlinePayment.Status.FAILURE
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.OnlinePaymentResponseFixture.anOnlinePaymentResponse
import static com.thebund1st.tiantong.core.OnlinePaymentResultNotification.Code.SUCCESS

class NotifyPaymentResultCommandHandlerTest extends Specification {

    private DomainEventPublisher eventPublisher = Mock()
    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private OnlinePaymentResultNotificationRepository onlinePaymentResponseRepository = Mock()
    private OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResponseIdentifierGenerator = Mock()
    private Clock clock = Mock()
    private NotifyPaymentResultCommandHandler target = new NotifyPaymentResultCommandHandler(
            onlinePaymentRepository,
            onlinePaymentResponseRepository,
            onlinePaymentResponseIdentifierGenerator,
            eventPublisher, clock)


    def "it should mark the online payment success and emit payment succeed event"() {
        given:
        def op = anOnlinePayment().build()
        def now = LocalDateTime.now()
        def notification = anOnlinePaymentNotification().amountIs(op.amount).succeed().sendTo(op).build()
        def responseId = anOnlinePaymentResponse().amountIs(op.amount).to(op.id).build().id

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now
        onlinePaymentResponseIdentifierGenerator.nextIdentifier() >> responseId

        when:
        //noinspection GroovyAssignabilityCheck
        target.handle(notification)

        then:
        assert op.lastModifiedAt == now
        assert op.status == OnlinePayment.Status.SUCCESS

        and:
        //noinspection GroovyAssignabilityCheck
        1 * onlinePaymentResponseRepository.save({
            it.id == responseId
            it.amount == op.amount
            it.text == notification.text
            it.onlinePaymentId == op.id
            it.code == SUCCESS
            it.createdAt == now
        })

        and:
        1 * eventPublisher.publish({
            it.onlinePaymentId == op.id
            it.onlinePaymentVersion == op.version
            it.correlation == op.correlation
            it.amount == op.amount
            it.when == now
        } as OnlinePaymentSucceededEvent)
    }

    def "it should mark the online payment failure"() {
        given:
        def op = anOnlinePayment().build()
        def now = LocalDateTime.now()
        def notification = anOnlinePaymentNotification().amountIs(op.amount).failed().sendTo(op).build()
        def responseId = anOnlinePaymentResponse().amountIs(op.amount).to(op.id).build().id

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op
        clock.now() >> now
        onlinePaymentResponseIdentifierGenerator.nextIdentifier() >> responseId

        when:
        //noinspection GroovyAssignabilityCheck
        target.handle(notification)

        then:
        assert op.lastModifiedAt == now
        assert op.status == FAILURE

        and:
        //noinspection GroovyAssignabilityCheck
        1 * onlinePaymentResponseRepository.save({
            it.id == responseId
            it.amount == op.amount
            it.text == notification.text
            it.onlinePaymentId == op.id
            it.code == FAILURE
            it.createdAt == now
        })

        and:
        0 * eventPublisher.publish(_)
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
        assert thrown.getMessage().contains("Online Payment [154] has been marked as SUCCESS, it failed to handle")
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
        assert thrown.getMessage().contains("Online Payment [154][100.0] failed to handle mismatch amount")
    }

}
