package com.thebund1st.tiantong.wechatpay.payment;

import com.thebund1st.tiantong.core.method.MethodMatcher;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;

public interface MethodBasedWeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator<P extends ProviderSpecificCreateOnlinePaymentRequest>
        extends WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator<P>, MethodMatcher {

}
