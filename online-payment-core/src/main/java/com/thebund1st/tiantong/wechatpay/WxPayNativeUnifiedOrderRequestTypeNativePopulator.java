package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;

public class WxPayNativeUnifiedOrderRequestTypeNativePopulator
        implements
        MethodBasedWxPayUnifiedOrderRequestProviderSpecificRequestPopulator<WeChatPayNativeSpecificOnlinePaymentRequest> {

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         WeChatPayNativeSpecificOnlinePaymentRequest providerSpecificRequest) {
        request.setProductId(providerSpecificRequest.getProductId());
    }

    @Override
    public boolean supports(String method) {
        return "NATIVE".equals(method);
    }
}
