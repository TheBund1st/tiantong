package com.thebund1st.tiantong.boot.application.scheduling;

import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobHandler;
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobStore;
import com.thebund1st.tiantong.application.scheduling.SyncOnlinePaymentResultsCommandHandler;
import com.thebund1st.tiantong.boot.core.OnlinePaymentResultSynchronizationProperties;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SchedulingConfiguration {
    private final Clock clock;
    private final OnlinePaymentResultSynchronizationJobStore onlinePaymentResultSynchronizationJobStore;
    private final OnlinePaymentResultSynchronizationJobHandler onlinePaymentResultSynchronizationJobHandler;
    @Autowired
    private OnlinePaymentResultSynchronizationProperties onlinePaymentResultSynchronizationProperties;

    @Bean
    public SyncOnlinePaymentResultsCommandHandler syncOnlinePaymentResultsCommandHandler() {
        SyncOnlinePaymentResultsCommandHandler bean = new SyncOnlinePaymentResultsCommandHandler(
                onlinePaymentResultSynchronizationJobStore,
                onlinePaymentResultSynchronizationJobHandler, clock);
        bean.setDelayInMinutes(onlinePaymentResultSynchronizationProperties.getDelay());
        return bean;
    }
}
