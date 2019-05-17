package com.thebund1st.tiantong.boot.provider;

import com.thebund1st.tiantong.core.payment.ProviderSpecificCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.provider.ProviderSpecificCloseOnlinePaymentGatewayDispatcher;
import com.thebund1st.tiantong.provider.MethodBasedCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.provider.MethodBasedCreateOnlinePaymentGateway;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentResultGateway;
import com.thebund1st.tiantong.provider.ProviderSpecificCreateOnlinePaymentGatewayDispatcher;
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

    private final List<MethodBasedCreateOnlinePaymentGateway> delegates;
    @Autowired
    private List<MethodBasedOnlinePaymentResultGateway> paymentResultGatewayGroup;
    @Autowired
    private List<MethodBasedCloseOnlinePaymentGateway> closeOnlinePaymentGatewayGroup;

    @Primary
    @Bean
    public ProviderSpecificCreateOnlinePaymentGateway onlinePaymentProviderGatewayDispatcher() {
        return new ProviderSpecificCreateOnlinePaymentGatewayDispatcher(delegates);
    }

    @Primary
    @Bean
    public OnlinePaymentResultGateway onlinePaymentResultGateway() {
        return new OnlinePaymentResultGatewayDispatcher(paymentResultGatewayGroup);
    }

    @Primary
    @Bean
    public ProviderSpecificCloseOnlinePaymentGateway closeOnlinePaymentGateway() {
        return new ProviderSpecificCloseOnlinePaymentGatewayDispatcher(closeOnlinePaymentGatewayGroup);
    }
}
