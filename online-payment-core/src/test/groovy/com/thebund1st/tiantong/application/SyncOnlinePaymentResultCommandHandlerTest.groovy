package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand
import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.OnlinePaymentResultFixture.anOnlinePaymentResult

class SyncOnlinePaymentResultCommandHandlerTest extends Specification {

    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private OnlinePaymentResultGateway onlinePaymentResultGateway = Mock()
    private NotifyPaymentResultCommandHandler notifyPaymentResultCommandHandler = Mock()
    private CloseOnlinePaymentCommandHandler closeOnlinePaymentCommandHandler = Mock()
    private Clock clock = Mock()
    private SyncOnlinePaymentResultCommandHandler subject =
            new SyncOnlinePaymentResultCommandHandler(onlinePaymentRepository,
                    onlinePaymentResultGateway,
                    notifyPaymentResultCommandHandler,
                    closeOnlinePaymentCommandHandler)

    def "it should sync online payment result"() {
        given:
        def onlinePayment = anOnlinePayment().build()
        def command = new SyncOnlinePaymentResultCommand(onlinePayment.id.value)
        def paymentResult = anOnlinePaymentResult().sendTo(onlinePayment).build()

        and:
        onlinePaymentRepository.mustFindBy(onlinePayment.id) >> onlinePayment
        onlinePaymentResultGateway.pull(onlinePayment) >> Optional.of(paymentResult)
        notifyPaymentResultCommandHandler.handle(paymentResult)

        when:
        def actual = subject.handle(command)

        then:
        assert actual == Optional.of(paymentResult)
    }

    def "it should skip sync online payment result given no result"() {
        given:
        def now = LocalDateTime.now()
        def onlinePayment = anOnlinePayment().createdAt(now.minusMinutes(1)).build()
        def command = new SyncOnlinePaymentResultCommand(onlinePayment.id.value)

        and:
        onlinePaymentRepository.mustFindBy(onlinePayment.id) >> onlinePayment
        onlinePaymentResultGateway.pull(onlinePayment) >> Optional.empty()
        clock.now() >> now

        when:
        def actual = subject.handle(command)

        then:
        assert !actual.isPresent()

        and:
        1 * closeOnlinePaymentCommandHandler.closeIfNecessary(onlinePayment)
    }

    def "it should skip sync online payment result given the online payment is not pending"() {
        given:
        def onlinePayment = anOnlinePayment().succeeded().build()
        def command = new SyncOnlinePaymentResultCommand(onlinePayment.id.value)

        and:
        onlinePaymentRepository.mustFindBy(onlinePayment.id) >> onlinePayment

        when:
        def actual = subject.handle(command)

        then:
        assert !actual.isPresent()
    }

}
