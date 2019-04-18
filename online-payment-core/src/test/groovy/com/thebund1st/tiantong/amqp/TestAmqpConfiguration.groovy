package com.thebund1st.tiantong.amqp


import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.annotation.PostConstruct

@Configuration
class TestAmqpConfiguration {

    @Autowired
    RabbitTemplate template

    @Bean
    Queue queue() {
        return new Queue("onlinePaymentSucceededQueue", false)
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("onlinePaymentSucceededTopic")
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        BindingBuilder.bind(queue).to(exchange).with("#")
    }

    @PostConstruct
    void enhanceRabbitTemplate() {
        template.setMessageConverter(new Jackson2JsonMessageConverter())
    }
}
