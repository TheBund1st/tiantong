package com.thebund1st.tiantong.boot.amqp;

import com.thebund1st.tiantong.amqp.AmqpDomainEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "tiantong.domainEventPublisherDelegate", name = "type", havingValue = "amqp")
@Configuration
public class AmqpConfiguration {

    @Bean(name = "domainEventPublisherDelegate")
    public AmqpDomainEventPublisher amqpDomainEventPublisher(RabbitTemplate rabbitTemplate) {
        return new AmqpDomainEventPublisher(rabbitTemplate, "onlinePaymentSucceededTopic");
    }
}
