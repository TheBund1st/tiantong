package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.MethodMatcherFunction;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentGateway;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Dispatching create online payment request based on {@link com.thebund1st.tiantong.core.OnlinePayment.Method}.
 */
@RequiredArgsConstructor
public class CreateOnlinePaymentProviderGatewayDispatcher
        implements ProviderSpecificCreateOnlinePaymentGateway,
        MethodMatcherFunction<MethodBasedOnlinePaymentProviderGateway, ProviderSpecificLaunchOnlinePaymentRequest> {

    private final List<MethodBasedOnlinePaymentProviderGateway> gatewayGroup;

    @Override
    public ProviderSpecificLaunchOnlinePaymentRequest create(OnlinePayment onlinePayment,
                                                             ProviderSpecificCreateOnlinePaymentRequest providerSpecificRequest) {
        return dispatch(gatewayGroup,
                onlinePayment::getMethod)
                .apply((methodAware) -> methodAware.create(onlinePayment, providerSpecificRequest),
                        () -> new NoSuchOnlinePaymentProviderGatewayException(onlinePayment));
    }
}
