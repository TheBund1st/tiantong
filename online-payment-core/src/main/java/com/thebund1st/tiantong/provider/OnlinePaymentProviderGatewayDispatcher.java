package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.MethodMatcherFunction;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import com.thebund1st.tiantong.core.ProviderSpecificUserAgentOnlinePaymentRequest;
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OnlinePaymentProviderGatewayDispatcher
        implements OnlinePaymentProviderGateway,
        MethodMatcherFunction<MethodBasedOnlinePaymentProviderGateway, ProviderSpecificUserAgentOnlinePaymentRequest> {

    private final List<MethodBasedOnlinePaymentProviderGateway> gatewayGroup;


    @Override
    public ProviderSpecificUserAgentOnlinePaymentRequest request(OnlinePayment onlinePayment,
                                                                 ProviderSpecificOnlinePaymentRequest providerSpecificRequest) {
        return dispatch(gatewayGroup,
                onlinePayment::getMethod)
                .apply((methodAware) -> methodAware.request(onlinePayment, providerSpecificRequest),
                () -> new NoSuchOnlinePaymentProviderGatewayException(onlinePayment));
    }
}
