package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;

import java.util.List;

public interface MethodBasedOnlinePaymentProviderGateway extends OnlinePaymentProviderGateway {
    List<OnlinePayment.Method> matchedMethods();
}
