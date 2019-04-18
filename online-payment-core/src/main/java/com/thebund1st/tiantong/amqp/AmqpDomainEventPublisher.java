package com.thebund1st.tiantong.amqp;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.events.OnlinePaymentSucceededEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class AmqpDomainEventPublisher implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final String onlinePaymentSucceeded;

    @SneakyThrows
    @Override
    public void publish(Object event) {
        OnlinePaymentSucceededEvent paymentSucceededEvent = (OnlinePaymentSucceededEvent) event;
        rabbitTemplate.convertAndSend(onlinePaymentSucceeded,
                paymentSucceededEvent.getCorrelation().getKey(), event);
    }

}
