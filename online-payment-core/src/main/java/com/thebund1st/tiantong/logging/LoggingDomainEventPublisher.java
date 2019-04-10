package com.thebund1st.tiantong.logging;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Just for testing, DO NOT use in production.
 */
@Slf4j
@RequiredArgsConstructor
public class LoggingDomainEventPublisher implements DomainEventPublisher {

    @Override
    public void publish(Object event) {
        log.info("Publishing {}", event.toString());
    }
}
