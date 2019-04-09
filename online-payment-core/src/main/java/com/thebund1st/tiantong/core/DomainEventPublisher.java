package com.thebund1st.tiantong.core;

public interface DomainEventPublisher {

    void publish(Object event);
}
