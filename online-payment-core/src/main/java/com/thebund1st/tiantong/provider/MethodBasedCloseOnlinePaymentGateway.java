package com.thebund1st.tiantong.provider;

import com.thebund1st.tiantong.core.CloseOnlinePaymentGateway;
import com.thebund1st.tiantong.core.MethodMatcher;

public interface MethodBasedCloseOnlinePaymentGateway
        extends CloseOnlinePaymentGateway, MethodMatcher {
}
