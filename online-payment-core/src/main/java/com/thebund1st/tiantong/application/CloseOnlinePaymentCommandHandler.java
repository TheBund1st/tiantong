package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.thebund1st.tiantong.core.OnlinePayment.shouldCloseSpecification;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class CloseOnlinePaymentCommandHandler {

    private final OnlinePaymentRepository onlinePaymentRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;

    public void closeIfNecessary(OnlinePayment onlinePayment) {
        if (shouldCloseSpecification(clock.now()).test(onlinePayment)) {
            LocalDateTime now = clock.now();
            onlinePayment.close(now);
            onlinePaymentRepository.update(onlinePayment);
            domainEventPublisher.publish(onlinePayment.toClosedEvent(now));
        }
    }
}
