package com.thebund1st.tiantong.boot.core;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class CoreConfiguration {
    @Bean
    public OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator() {
        return () -> OnlinePayment.Identifier.of(UUID.randomUUID().toString().replace("-", ""));
    }
}
