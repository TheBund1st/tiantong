package com.thebund1st.tiantong.wechatpay.jsapi;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.method.Method;
import com.thebund1st.tiantong.wechatpay.payment.MethodBasedWeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator;

import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayJsApi;

public class WeChatPayJsApiCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator
        implements
        MethodBasedWeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator<WeChatPayJsApiCreateOnlinePaymentRequest> {

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         WeChatPayJsApiCreateOnlinePaymentRequest providerSpecificRequest) {
        request.setOpenid(providerSpecificRequest.getOpenId());
    }

    @Override
    public boolean supports(Method method) {
        return weChatPayJsApi().equals(method);
    }
}
