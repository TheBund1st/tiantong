package com.thebund1st.tiantong.wechatpay.payment;

import com.thebund1st.tiantong.core.method.MethodMatcher;
import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;

public interface MethodBasedWeChatPayLaunchOnlinePaymentRequestAssembler
        <P extends ProviderSpecificLaunchOnlinePaymentRequest>
        extends WeChatPayLaunchOnlinePaymentRequestAssembler<P>, MethodMatcher {

}
