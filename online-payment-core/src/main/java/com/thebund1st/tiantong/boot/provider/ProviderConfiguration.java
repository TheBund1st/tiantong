package com.thebund1st.tiantong.boot.provider;

import com.thebund1st.tiantong.core.payment.ProviderSpecificCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentGateway;
import com.thebund1st.tiantong.core.payment.ProviderSpecificPullOnlinePaymentResultGateway;
import com.thebund1st.tiantong.provider.ProviderSpecificCloseOnlinePaymentGatewayDispatcher;
import com.thebund1st.tiantong.provider.MethodBasedCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.provider.MethodBasedCreateOnlinePaymentGateway;
import com.thebund1st.tiantong.provider.MethodBasedPullOnlinePaymentResultGateway;
import com.thebund1st.tiantong.provider.ProviderSpecificCreateOnlinePaymentGatewayDispatcher;
import com.thebund1st.tiantong.provider.ProviderSpecificPullOnlinePaymentResultGatewayDispatcher;
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
    private List<MethodBasedPullOnlinePaymentResultGateway> paymentResultGatewayGroup;
    @Autowired
    private List<MethodBasedCloseOnlinePaymentGateway> closeOnlinePaymentGatewayGroup;

    @Primary
    @Bean
    public ProviderSpecificCreateOnlinePaymentGateway onlinePaymentProviderGatewayDispatcher() {
        return new ProviderSpecificCreateOnlinePaymentGatewayDispatcher(delegates);
    }

    @Primary
    @Bean
    public ProviderSpecificPullOnlinePaymentResultGateway onlinePaymentResultGateway() {
        return new ProviderSpecificPullOnlinePaymentResultGatewayDispatcher(paymentResultGatewayGroup);
    }

    @Primary
    @Bean
    public ProviderSpecificCloseOnlinePaymentGateway closeOnlinePaymentGateway() {
        return new ProviderSpecificCloseOnlinePaymentGatewayDispatcher(closeOnlinePaymentGatewayGroup);
    }
}
