package com.thebund1st.tiantong.core;

import com.thebund1st.tiantong.events.OnlinePaymentSuccessEvent;

public interface DomainEventPublisher {

    void publish(OnlinePaymentSuccessEvent event);
}
