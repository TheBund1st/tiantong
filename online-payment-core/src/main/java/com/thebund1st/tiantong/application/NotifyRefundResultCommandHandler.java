package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.NotifyRefundResultCommand;
import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import com.thebund1st.tiantong.core.refund.OnlineRefundRepository;
import com.thebund1st.tiantong.events.OnlineRefundSucceededEvent;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotifyRefundResultCommandHandler {

    private final OnlineRefundRepository onlineRefundRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;

    public void handle(NotifyRefundResultCommand command) {
        LocalDateTime now = clock.now();
        OnlineRefund onlineRefund = onlineRefundRepository.mustFindBy(command.getOnlineRefundId());
        onlineRefund.markSuccess(now);
        domainEventPublisher.publish(toOnlineRefundSuccessEvent(onlineRefund));
    }

    private OnlineRefundSucceededEvent toOnlineRefundSuccessEvent(OnlineRefund onlineRefund) {
        OnlineRefundSucceededEvent event = new OnlineRefundSucceededEvent();
        event.setWhen(onlineRefund.getLastModifiedAt());
        event.setOnlineRefundId(onlineRefund.getId());
        event.setOnlineRefundVersion(onlineRefund.getVersion());
        event.setRefundAmount(onlineRefund.getAmount());
        event.setOnlinePaymentId(onlineRefund.getOnlinePaymentId());
        event.setPaymentAmount(onlineRefund.getOnlinePaymentAmount());
        event.setCorrelation(onlineRefund.getCorrelation());
        return event;
    }

}
