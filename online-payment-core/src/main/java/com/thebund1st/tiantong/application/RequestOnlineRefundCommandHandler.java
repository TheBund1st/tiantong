package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.RequestOnlineRefundCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import com.thebund1st.tiantong.core.refund.OnlineRefundIdentifierGenerator;
import com.thebund1st.tiantong.core.refund.OnlineRefundRepository;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class RequestOnlineRefundCommandHandler {

    private final OnlinePaymentRepository onlinePaymentRepository;
    private final OnlineRefundIdentifierGenerator onlineRefundIdentifierGenerator;
    private final OnlineRefundRepository onlineRefundRepository;
    private final Clock clock;

    public OnlineRefund handle(RequestOnlineRefundCommand command) {
        OnlinePayment onlinePayment = onlinePaymentRepository
                .mustFindBy(OnlinePayment.Identifier.of(command.getOnlinePaymentId()));
        OnlineRefund onlineRefund = anOnlineRefundFor(onlinePayment);
        onlineRefundRepository.save(onlineRefund);
        return onlineRefund;
    }

    private OnlineRefund anOnlineRefundFor(OnlinePayment onlinePayment) {
        LocalDateTime now = clock.now();
        OnlineRefund onlineRefund = new OnlineRefund();
        onlineRefund.setId(onlineRefundIdentifierGenerator.nextIdentifier());
        onlineRefund.setAmount(onlinePayment.getAmount());
        onlineRefund.setOnlinePaymentId(onlinePayment.getId());
        onlineRefund.setOnlinePaymentAmount(onlinePayment.getAmount());
        onlineRefund.setCreatedAt(now);
        onlineRefund.setLastModifiedAt(now);
        return onlineRefund;
    }
}
