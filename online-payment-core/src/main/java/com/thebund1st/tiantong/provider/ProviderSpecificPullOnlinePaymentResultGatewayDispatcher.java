package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.method.MethodMatcherFunction;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.payment.ProviderSpecificPullOnlinePaymentResultGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProviderSpecificPullOnlinePaymentResultGatewayDispatcher
        implements ProviderSpecificPullOnlinePaymentResultGateway,
        MethodMatcherFunction<MethodBasedPullOnlinePaymentResultGateway, Optional<OnlinePaymentResultNotification>> {

    private final List<MethodBasedPullOnlinePaymentResultGateway> gatewayGroup;

    @Override
    public Optional<OnlinePaymentResultNotification> pull(OnlinePayment onlinePayment) {
        return dispatch(gatewayGroup, onlinePayment::getMethod)
                .apply((methodAware) -> methodAware.pull(onlinePayment),
                        () -> new NoSuchOnlinePaymentProviderGatewayException(onlinePayment));
    }

}
