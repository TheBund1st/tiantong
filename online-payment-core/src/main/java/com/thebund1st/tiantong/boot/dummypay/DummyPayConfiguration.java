package com.thebund1st.tiantong.boot.dummypay;

import com.thebund1st.tiantong.dummypay.DummyOnlinePaymentProviderGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DummyPayConfiguration {

    @Bean
    public DummyOnlinePaymentProviderGateway dummyOnlinePaymentProviderGateway() {
        return new DummyOnlinePaymentProviderGateway();
    }
}
