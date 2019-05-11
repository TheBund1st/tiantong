package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OnlinePaymentResultGatewayDispatcher implements OnlinePaymentResultGateway {

    private final List<MethodBasedOnlinePaymentResultGateway> gatewayGroup;

    @Override
    public Optional<OnlinePaymentResultNotification> pull(OnlinePayment onlinePayment) {
        Optional<MethodBasedOnlinePaymentResultGateway> gateway = gatewayGroup.stream()
                .filter(g -> g.supports(onlinePayment.getMethod().getValue()))
                .findFirst();
        if (gateway.isPresent()) {
            return gateway.get().pull(onlinePayment);
        } else {
            throw new NoSuchOnlinePaymentProviderGatewayException(onlinePayment);
        }
    }
}
