package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.payment.ProviderSpecificCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.core.method.MethodMatcherConsumer;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProviderSpecificCloseOnlinePaymentGatewayDispatcher
        implements ProviderSpecificCloseOnlinePaymentGateway,
        MethodMatcherConsumer<MethodBasedCloseOnlinePaymentGateway> {

    private final List<MethodBasedCloseOnlinePaymentGateway> gatewayGroup;


    @Override
    public void close(OnlinePayment onlinePayment) {
        dispatch(gatewayGroup,
                onlinePayment::getMethod,
                (gateway) -> gateway.close(onlinePayment),
                () -> new NoSuchOnlinePaymentProviderGatewayException(onlinePayment));
    }
}
