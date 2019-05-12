package com.thebund1st.tiantong.boot.application.scheduling;

import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobHandler;
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobStore;
import com.thebund1st.tiantong.application.scheduling.SyncOnlinePaymentResultsCommandHandler;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SchedulingConfiguration {
    private final Clock clock;
    private final OnlinePaymentResultSynchronizationJobStore onlinePaymentResultSynchronizationJobStore;
    private final OnlinePaymentResultSynchronizationJobHandler onlinePaymentResultSynchronizationJobHandler;


    @Bean
    public SyncOnlinePaymentResultsCommandHandler syncOnlinePaymentResultsCommandHandler() {
        return new SyncOnlinePaymentResultsCommandHandler(onlinePaymentResultSynchronizationJobStore,
                onlinePaymentResultSynchronizationJobHandler, clock);
    }
}
