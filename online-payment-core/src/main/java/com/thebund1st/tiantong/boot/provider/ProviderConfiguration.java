package com.thebund1st.tiantong.boot.provider;

import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentProviderGateway;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentResultGateway;
import com.thebund1st.tiantong.provider.OnlinePaymentProviderGatewayDispatcher;
import com.thebund1st.tiantong.provider.OnlinePaymentResultGatewayDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ProviderConfiguration {

    private final List<MethodBasedOnlinePaymentProviderGateway> delegates;
    @Autowired
    private List<MethodBasedOnlinePaymentResultGateway> paymentResultGatewayGroup;

    @Primary
    @Bean
    public OnlinePaymentProviderGateway onlinePaymentProviderGatewayDispatcher() {
        return new OnlinePaymentProviderGatewayDispatcher(delegates);
    }

    @Primary
    @Bean
    public OnlinePaymentResultGateway onlinePaymentResultGateway() {
        return new OnlinePaymentResultGatewayDispatcher(paymentResultGatewayGroup);
    }
}
