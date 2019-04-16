package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.core.refund.OnlineRefundIdentifierGenerator
import com.thebund1st.tiantong.core.refund.OnlineRefundRepository
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.LocalDateTime

import static com.thebund1st.tiantong.commands.RequestOnlineRefundCommandFixture.aRequestOnlineRefundCommand
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.refund.OnlineRefund.Status.PENDING
import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund

class RequestOnlineRefundCommandHandlerTest extends Specification {

    private RequestOnlineRefundCommandHandler requestOnlineRefundCommandHandler

    private OnlinePaymentRepository onlinePaymentRepository = Mock()

    private OnlineRefundIdentifierGenerator onlineRefundIdentifierGenerator = Mock()

    private OnlineRefundRepository onlineRefundRepository = Mock()

    private Clock clock = Mock()

    def setup() {
        this.requestOnlineRefundCommandHandler =
                new RequestOnlineRefundCommandHandler(onlinePaymentRepository,
                        onlineRefundIdentifierGenerator, onlineRefundRepository, clock)
    }

    def "it should create a refund for a given online payment"() {
        given:
        def op = anOnlinePayment().build()
        def or = anOnlineRefund().with(op).build()
        def command = aRequestOnlineRefundCommand().with(or).build()
        def now = LocalDateTime.now()

        and:
        onlinePaymentRepository.mustFindBy(op.id) >> op

        onlineRefundIdentifierGenerator.nextIdentifier() >> or.id

        clock.now() >> now

        when:
        def refund = requestOnlineRefundCommandHandler.handle(command)

        then:
        assert refund.id == or.id
        assert refund.amount == op.amount
        assert refund.onlinePaymentId == op.id
        assert refund.onlinePaymentAmount == op.amount
        assert refund.correlation == op.correlation
        assert refund.method == op.method
        assert refund.status == PENDING
        assert refund.createdAt == now
        assert refund.lastModifiedAt == now

        and:
        1 * onlineRefundRepository.save(or)
    }
}
