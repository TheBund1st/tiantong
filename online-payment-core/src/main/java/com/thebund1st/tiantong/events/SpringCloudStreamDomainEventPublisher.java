package com.thebund1st.tiantong.events;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@RequiredArgsConstructor
public class SpringCloudStreamDomainEventPublisher implements DomainEventPublisher {

    private final MessageChannel output;

    @Override
    public void publish(Object event) {
        output.send(MessageBuilder.withPayload(event).build());
    }
}
