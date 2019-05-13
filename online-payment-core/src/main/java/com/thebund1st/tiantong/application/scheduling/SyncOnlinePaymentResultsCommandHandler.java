package com.thebund1st.tiantong.application.scheduling;

import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class SyncOnlinePaymentResultsCommandHandler {

    private final OnlinePaymentResultSynchronizationJobStore onlinePaymentResultSynchronizationJobStore;

    private final OnlinePaymentResultSynchronizationJobHandler onlinePaymentResultSynchronizationJobHandler;

    private final Clock clock;

    @Setter
    private int batchSize = 20;

    @Setter
    private int delayInMinutes = 3;


    public void handle() {
        LocalDateTime createdAt = clock.now().minusMinutes(delayInMinutes);
        doSyncOnlinePaymentResultsBy(createdAt, PageRequest.of(0, batchSize));
    }

    private void doSyncOnlinePaymentResultsBy(LocalDateTime localDateTime, Pageable pageable) {
        Page<OnlinePaymentResultSynchronizationJob> page = onlinePaymentResultSynchronizationJobStore
                .find(localDateTime, pageable);
        page.forEach(command -> {
            try {
                onlinePaymentResultSynchronizationJobHandler.handle(command);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        if (page.hasNext()) {
            doSyncOnlinePaymentResultsBy(localDateTime, page.nextPageable());
        }
    }
}
