package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException;

import java.util.Map;

public class OnlinePaymentProviderGatewayDispatcher implements OnlinePaymentProviderGateway {

    private Map<OnlinePayment.Method, OnlinePaymentProviderGateway> delegates;

    public OnlinePaymentProviderGatewayDispatcher(Map<OnlinePayment.Method, OnlinePaymentProviderGateway> delegates) {
        this.delegates = delegates;
    }

    @Override
    public ProviderSpecificRequest request(OnlinePayment onlinePayment) {
        OnlinePaymentProviderGateway delegate = delegates.get(onlinePayment.getMethod());
        if (delegate == null) {
            throw new NoSuchOnlinePaymentProviderGatewayException(onlinePayment);
        }
        return delegate.request(onlinePayment);
    }
}
