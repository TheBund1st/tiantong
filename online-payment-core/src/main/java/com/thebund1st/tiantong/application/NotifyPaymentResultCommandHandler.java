package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;
import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResponse;
import com.thebund1st.tiantong.core.OnlinePaymentResponseIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentResponseRepository;
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
    private final OnlinePaymentResponseRepository onlinePaymentResponseRepository;
    private final OnlinePaymentResponseIdentifierGenerator onlinePaymentResponseIdentifierGenerator;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;

    public void handle(NotifyPaymentResultCommand command) {
        LocalDateTime now = clock.now();
        OnlinePayment op = onlinePaymentRepository.mustFindBy(command.getOnlinePaymentId());
        OnlinePaymentResponse response = toResponse(command, now);
        List<Object> events = op.on(command, now);
        onlinePaymentResponseRepository.save(response);
        events.forEach(domainEventPublisher::publish);
    }

    private OnlinePaymentResponse toResponse(NotifyPaymentResultCommand command, LocalDateTime now) {
        OnlinePaymentResponse response = new OnlinePaymentResponse();
        response.setId(onlinePaymentResponseIdentifierGenerator.nextIdentifier());
        response.setOnlinePaymentId(command.getOnlinePaymentId());
        response.setAmount(command.getAmount());
        response.setText(command.getText());
        response.setCreatedAt(now);
        response.setCode(OnlinePaymentResponse.Code.SUCCESS);
        return response;
    }

}
