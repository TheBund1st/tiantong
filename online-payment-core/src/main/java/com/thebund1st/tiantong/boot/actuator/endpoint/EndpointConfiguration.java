package com.thebund1st.tiantong.boot.actuator.endpoint;

import com.thebund1st.tiantong.application.scheduling.SyncOnlinePaymentResultsCommandHandler;
import com.thebund1st.tiantong.web.ops.SyncOnlinePaymentResultActuatorEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfiguration {

    @Autowired
    private SyncOnlinePaymentResultsCommandHandler syncOnlinePaymentResultsCommandHandler;

    @Bean
    public SyncOnlinePaymentResultActuatorEndpoint syncOnlinePaymentResultActuatorEndpoint() {
        return new SyncOnlinePaymentResultActuatorEndpoint(syncOnlinePaymentResultsCommandHandler);
    }

}
