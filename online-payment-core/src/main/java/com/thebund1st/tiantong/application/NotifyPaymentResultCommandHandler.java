package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;
import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationRepository;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotifyPaymentResultCommandHandler {

    private final OnlinePaymentRepository onlinePaymentRepository;
    private final OnlinePaymentResultNotificationRepository onlinePaymentResultNotificationRepository;
    private final OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResultNotificationIdentifierGenerator;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;

    public void handle(NotifyPaymentResultCommand command) {
        LocalDateTime now = clock.now();
        OnlinePayment op = onlinePaymentRepository.mustFindBy(command.getOnlinePaymentId());
        OnlinePaymentResultNotification response = toResponse(command, now);
        List<Object> events = op.on(command, now);
        onlinePaymentResultNotificationRepository.save(response);
        events.forEach(domainEventPublisher::publish);
    }

    private OnlinePaymentResultNotification toResponse(NotifyPaymentResultCommand command, LocalDateTime now) {
        OnlinePaymentResultNotification response = new OnlinePaymentResultNotification();
        response.setId(onlinePaymentResultNotificationIdentifierGenerator.nextIdentifier());
        response.setOnlinePaymentId(command.getOnlinePaymentId());
        response.setAmount(command.getAmount());
        response.setText(command.getText());
        response.setCreatedAt(now);
        response.setCode(OnlinePaymentResultNotification.Code.SUCCESS);
        return response;
    }

}
