package com.thebund1st.tiantong.amqp;

import com.thebund1st.tiantong.core.DomainEventPublisher;

public interface AmqpDomainEventPublisher extends DomainEventPublisher {

    boolean supports(Object event);

}
