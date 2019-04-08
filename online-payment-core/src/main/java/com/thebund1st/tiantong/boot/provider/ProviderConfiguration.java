package com.thebund1st.tiantong.boot.provider;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfiguration {

    @Bean
    public OnlinePaymentProviderGateway onlinePaymentProviderGateway() {
        return new OnlinePaymentProviderGateway() {

            @Override
            public ProviderSpecificRequest request(OnlinePayment onlinePayment) {
                return null;
            }
        };
    }
}
