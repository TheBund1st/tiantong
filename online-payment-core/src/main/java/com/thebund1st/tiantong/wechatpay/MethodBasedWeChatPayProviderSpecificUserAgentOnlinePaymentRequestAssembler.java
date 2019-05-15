package com.thebund1st.tiantong.wechatpay;

import com.thebund1st.tiantong.core.MethodMatcher;
import com.thebund1st.tiantong.core.ProviderSpecificUserAgentOnlinePaymentRequest;

public interface MethodBasedWeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler<P extends ProviderSpecificUserAgentOnlinePaymentRequest>
        extends WeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler<P>, MethodMatcher {

}
