package com.thebund1st.tiantong.amqp;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

@RequiredArgsConstructor
public class AmqpDomainEventPublisherDispatcher implements DomainEventPublisher {

    private final List<AmqpDomainEventPublisher> publisherGroup;

    @SneakyThrows
    @Override
    public void publish(Object event) {
        publisherGroup.stream()
                .filter(p -> p.supports(event))
                .forEach(p -> p.publish(event));
    }

}
