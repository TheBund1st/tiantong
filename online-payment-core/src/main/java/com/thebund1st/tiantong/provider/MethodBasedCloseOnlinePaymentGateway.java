package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.payment.ProviderSpecificCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.core.method.MethodMatcher;

public interface MethodBasedCloseOnlinePaymentGateway
        extends ProviderSpecificCloseOnlinePaymentGateway, MethodMatcher {
}
