package com.thebund1st.tiantong.amqp;

import com.thebund1st.tiantong.events.OnlinePaymentSucceededEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class AmqpOnlinePaymentSucceededEventPublisher implements AmqpDomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange;

    @SneakyThrows
    @Override
    public void publish(Object event) {
        OnlinePaymentSucceededEvent paymentSucceededEvent = (OnlinePaymentSucceededEvent) event;
        rabbitTemplate.convertAndSend(exchange,
                paymentSucceededEvent.getCorrelation().getKey(), event);
    }

    @Override
    public boolean supports(Object event) {
        return event.getClass().isAssignableFrom(OnlinePaymentSucceededEvent.class);
    }
}
