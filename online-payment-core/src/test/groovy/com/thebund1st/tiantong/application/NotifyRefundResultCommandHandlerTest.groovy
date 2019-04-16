package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.core.DomainEventPublisher
import com.thebund1st.tiantong.core.refund.OnlineRefund
import com.thebund1st.tiantong.core.refund.OnlineRefundRepository
import com.thebund1st.tiantong.events.OnlineRefundSucceededEvent
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.LocalDateTime

import static com.thebund1st.tiantong.commands.OnlineRefundNotificationFixture.anOnlineRefundNotification
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund

class NotifyRefundResultCommandHandlerTest extends Specification {

    private DomainEventPublisher eventPublisher = Mock()
    private OnlineRefundRepository onlineRefundRepository = Mock()
    private Clock clock = Mock()
    private NotifyRefundResultCommandHandler target = new NotifyRefundResultCommandHandler(
            onlineRefundRepository,
            eventPublisher, clock)


    def "it should mark the online refund success and emit refund succeed event"() {
        given:
        def op = anOnlinePayment().build()
        def or = anOnlineRefund().with(op).build()
        def now = LocalDateTime.now()
        def command = anOnlineRefundNotification().with(or).build()

        and:
        onlineRefundRepository.mustFindBy(or.getId()) >> or
        clock.now() >> now

        when:
        target.handle(command)

        then:
        assert or.status == OnlineRefund.Status.SUCCESS
        assert or.lastModifiedAt == now

        and:
        1 * eventPublisher.publish({
            it.onlineRefundId == or.id
            it.onlineRefundVersion == or.version
            it.refundAmount == or.amount
            it.onlinePaymentId == or.onlinePaymentId
            it.paymentAmount == or.onlinePaymentAmount
            it.correlation == or.correlation
            it.when == now
        } as OnlineRefundSucceededEvent)
    }


}
