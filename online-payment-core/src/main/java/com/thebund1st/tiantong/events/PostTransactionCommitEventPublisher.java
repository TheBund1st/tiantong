package com.thebund1st.tiantong.events;

import com.thebund1st.tiantong.core.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.support.TransactionSynchronization;

import static org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization;

@RequiredArgsConstructor
public class PostTransactionCommitEventPublisher implements EventPublisher {

    private final EventPublisher eventPublisher;

    @Override
    public void publish(Object event) {
        registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                eventPublisher.publish(event);
            }
        });
    }
}
