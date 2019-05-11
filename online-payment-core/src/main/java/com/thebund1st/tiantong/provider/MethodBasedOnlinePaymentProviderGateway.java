package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.MethodMatcher;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;

public interface MethodBasedOnlinePaymentProviderGateway
        extends OnlinePaymentProviderGateway, MethodMatcher {
}
