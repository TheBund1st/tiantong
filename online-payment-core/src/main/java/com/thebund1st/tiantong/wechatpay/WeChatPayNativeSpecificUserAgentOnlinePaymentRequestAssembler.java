package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class WeChatPayNativeSpecificUserAgentOnlinePaymentRequestAssembler
        implements MethodBasedWeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler
        <WeChatPayNativeSpecificUserAgentOnlinePaymentRequest> {

    @Override
    public boolean supports(OnlinePayment.Method method) {
        return OnlinePayment.Method.of("WECHAT_PAY_NATIVE").equals(method);
    }

    @Override
    public WeChatPayNativeSpecificUserAgentOnlinePaymentRequest from(OnlinePayment onlinePayment, WxPayUnifiedOrderResult result) {
        WeChatPayNativeSpecificUserAgentOnlinePaymentRequest returning = new WeChatPayNativeSpecificUserAgentOnlinePaymentRequest();
        returning.setCodeUrl(result.getCodeURL());
        return returning;
    }
}
