package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.method.MethodMatcher;
import com.thebund1st.tiantong.core.payment.ProviderSpecificPullOnlinePaymentResultGateway;

public interface MethodBasedPullOnlinePaymentResultGateway extends MethodMatcher, ProviderSpecificPullOnlinePaymentResultGateway {
}
