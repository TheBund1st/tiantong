package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;

public class WxPayUnifiedOrderRequestTypeJsApiPopulator
        implements
        MethodBasedWxPayUnifiedOrderRequestProviderSpecificRequestPopulator<WeChatPayJsApiSpecificOnlinePaymentRequest> {

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         WeChatPayJsApiSpecificOnlinePaymentRequest providerSpecificRequest) {
        request.setOpenid(providerSpecificRequest.getOpenId());
    }

    @Override
    public boolean supports(String method) {
        return "JSAPI".equals(method);
    }
}
