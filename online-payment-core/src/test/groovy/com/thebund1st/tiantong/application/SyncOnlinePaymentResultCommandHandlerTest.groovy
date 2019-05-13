package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand
import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway
import spock.lang.Specification

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.OnlinePaymentResultFixture.anOnlinePaymentResult

class SyncOnlinePaymentResultCommandHandlerTest extends Specification {

    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private OnlinePaymentResultGateway onlinePaymentResultGateway = Mock()
    private NotifyPaymentResultCommandHandler notifyPaymentResultCommandHandler = Mock()
    private SyncOnlinePaymentResultCommandHandler subject =
            new SyncOnlinePaymentResultCommandHandler(onlinePaymentRepository,
                    onlinePaymentResultGateway, notifyPaymentResultCommandHandler)

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
        def onlinePayment = anOnlinePayment().build()
        def command = new SyncOnlinePaymentResultCommand(onlinePayment.id.value)

        and:
        onlinePaymentRepository.mustFindBy(onlinePayment.id) >> onlinePayment
        onlinePaymentResultGateway.pull(onlinePayment) >> Optional.empty()

        when:
        def actual = subject.handle(command)

        then:
        assert !actual.isPresent()
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
