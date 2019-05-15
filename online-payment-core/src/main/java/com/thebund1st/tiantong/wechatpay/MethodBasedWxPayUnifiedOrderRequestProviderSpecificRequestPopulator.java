package com.thebund1st.tiantong.wechatpay;

import com.thebund1st.tiantong.core.MethodMatcher;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;

public interface MethodBasedWxPayUnifiedOrderRequestProviderSpecificRequestPopulator<P extends ProviderSpecificOnlinePaymentRequest>
        extends WxPayUnifiedOrderRequestProviderSpecificRequestPopulator<P>, MethodMatcher {

}
