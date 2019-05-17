package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand
import com.thebund1st.tiantong.core.payment.ProviderSpecificCloseOnlinePaymentGateway
import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.OnlinePaymentResultFixture.anOnlinePaymentResult

class SyncOnlinePaymentResultCommandHandlerTest extends Specification {

    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private OnlinePaymentResultGateway onlinePaymentResultGateway = Mock()
    private ProviderSpecificCloseOnlinePaymentGateway closeOnlinePaymentGateway = Mock()
    private NotifyOnlinePaymentResultCommandHandler notifyPaymentResultCommandHandler = Mock()
    private Clock clock = Mock()
    private SyncOnlinePaymentResultCommandHandler subject =
            new SyncOnlinePaymentResultCommandHandler(onlinePaymentRepository,
                    onlinePaymentResultGateway,
                    notifyPaymentResultCommandHandler,
                    closeOnlinePaymentGateway,
                    clock)

    def "it should sync online payment result"() {
        given:
        def created = LocalDateTime.now()
        def onlinePayment = anOnlinePayment().createdAt(created).expires(Duration.ofMinutes(30)).build()
        def command = new SyncOnlinePaymentResultCommand(onlinePayment.id.value)
        def paymentResult = anOnlinePaymentResult().sendTo(onlinePayment).build()

        and:
        onlinePaymentRepository.mustFindBy(onlinePayment.id) >> onlinePayment
        onlinePaymentResultGateway.pull(onlinePayment) >> Optional.of(paymentResult)
        notifyPaymentResultCommandHandler.handle(paymentResult)
        clock.now() >> created.plusMinutes(31)

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

    def "it should close online payment given expires"() {
        given:
        def created = LocalDateTime.now()
        def onlinePayment = anOnlinePayment().createdAt(created).expires(Duration.ofMinutes(30)).build()
        def command = new SyncOnlinePaymentResultCommand(onlinePayment.id.value)

        and:
        onlinePaymentRepository.mustFindBy(onlinePayment.id) >> onlinePayment
        onlinePaymentResultGateway.pull(onlinePayment) >> Optional.empty()
        clock.now() >> created.plusMinutes(31)

        when:
        def actual = subject.handle(command)

        then:
        assert actual == Optional.empty()

        and:
        1 * closeOnlinePaymentGateway.close(onlinePayment)

    }

}
