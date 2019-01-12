package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.OnlinePaymentFailureNotification;
import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification;
import com.thebund1st.tiantong.core.EventPublisher;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.events.OnlinePaymentSuccessEvent;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class OnlinePaymentNotificationSubscriber {

    private final OnlinePaymentRepository onlinePaymentRepository;
    private final EventPublisher eventPublisher;
    private final Clock clock;

    public void handle(OnlinePaymentSuccessNotification command) {
        LocalDateTime now = clock.now();
        OnlinePayment op = onlinePaymentRepository.mustFindBy(command.getOnlinePaymentId());
        op.on(command, now);
        eventPublisher.publish(toOnlinePaymentSuccessEvent(op, now));
    }

    public void handle(OnlinePaymentFailureNotification command) {
        LocalDateTime now = clock.now();
        OnlinePayment op = onlinePaymentRepository.mustFindBy(command.getOnlinePaymentId());
        op.on(command, now);
        eventPublisher.publish(toOnlinePaymentSuccessEvent(op, now));
    }

    private OnlinePaymentSuccessEvent toOnlinePaymentSuccessEvent(OnlinePayment op, LocalDateTime now) {
        OnlinePaymentSuccessEvent event = new OnlinePaymentSuccessEvent();
        event.setWhen(now);
        event.setOnlinePaymentId(op.getId());
        event.setOnlinePaymentVersion(op.getVersion());
        event.setCorrelation(op.getCorrelation());
        event.setAmount(op.getAmount());
        return event;
    }
}
