package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.OnlinePaymentFailureNotification;
import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification;
import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResponse;
import com.thebund1st.tiantong.core.OnlinePaymentResponseIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentResponseRepository;
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
    private final OnlinePaymentResponseRepository onlinePaymentResponseRepository;
    private final OnlinePaymentResponseIdentifierGenerator onlinePaymentResponseIdentifierGenerator;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;

    public void handle(OnlinePaymentSuccessNotification command) {
        LocalDateTime now = clock.now();
        OnlinePayment op = onlinePaymentRepository.mustFindBy(command.getOnlinePaymentId());
        OnlinePaymentResponse response = toResponse(command, now);
        op.on(command, now);
        onlinePaymentResponseRepository.save(response);
        domainEventPublisher.publish(toOnlinePaymentSuccessEvent(op, now));
    }

    private OnlinePaymentResponse toResponse(OnlinePaymentSuccessNotification command, LocalDateTime now) {
        OnlinePaymentResponse response = new OnlinePaymentResponse();
        response.setId(onlinePaymentResponseIdentifierGenerator.nextIdentifier());
        response.setOnlinePaymentId(command.getOnlinePaymentId());
        response.setAmount(command.getAmount());
        response.setText(command.getText());
        response.setCreatedAt(now);
        response.setCode(OnlinePaymentResponse.Code.SUCCESS);
        return response;
    }

    public void handle(OnlinePaymentFailureNotification command) {
        LocalDateTime now = clock.now();
        OnlinePayment op = onlinePaymentRepository.mustFindBy(command.getOnlinePaymentId());
        op.on(command, now);
        domainEventPublisher.publish(toOnlinePaymentSuccessEvent(op, now));
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
