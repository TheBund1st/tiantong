package com.thebund1st.tiantong.boot.provider;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.provider.OnlinePaymentResultGatewayDispatcher;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentProviderGateway;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentResultGateway;
import com.thebund1st.tiantong.provider.OnlinePaymentProviderGatewayDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ProviderConfiguration {

    private final List<MethodBasedOnlinePaymentProviderGateway> delegates;
    @Autowired
    private List<MethodBasedOnlinePaymentResultGateway> paymentResultGatewayGroup;

    @Primary
    @Bean
    public OnlinePaymentProviderGateway onlinePaymentProviderGatewayDispatcher() {
        Map<OnlinePayment.Method, OnlinePaymentProviderGateway> delegateMap = new HashMap<>();
        delegates.forEach(d -> {
            d.matchedMethods().forEach(m -> {
                delegateMap.put(m, d);
            });
        });
        return new OnlinePaymentProviderGatewayDispatcher(delegateMap);
    }

    @Primary
    @Bean
    public OnlinePaymentResultGateway onlinePaymentResultGateway() {
        return new OnlinePaymentResultGatewayDispatcher(paymentResultGatewayGroup);
    }
}
