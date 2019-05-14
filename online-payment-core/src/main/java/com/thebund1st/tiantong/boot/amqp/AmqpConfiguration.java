package com.thebund1st.tiantong.boot.amqp;

import com.thebund1st.tiantong.amqp.AmqpDomainEventPublisher;
import com.thebund1st.tiantong.amqp.AmqpDomainEventPublisherDispatcher;
import com.thebund1st.tiantong.amqp.AmqpOnlinePaymentSucceededEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConditionalOnProperty(prefix = "tiantong.domainEventPublisherDelegate", name = "type", havingValue = "amqp")
@Configuration
public class AmqpConfiguration {

    @Bean
    public AmqpOnlinePaymentSucceededEventPublisher amqpOnlinePaymentSuccededEventPublisher(RabbitTemplate rabbitTemplate) {
        return new AmqpOnlinePaymentSucceededEventPublisher(rabbitTemplate, "onlinePaymentSucceededTopic");
    }

    @Bean(name = "domainEventPublisherDelegate")
    public AmqpDomainEventPublisherDispatcher amqpDomainEventPublisher(List<AmqpDomainEventPublisher> publisherGroup) {
        return new AmqpDomainEventPublisherDispatcher(publisherGroup);
    }
}
