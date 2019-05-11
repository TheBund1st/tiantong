package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.OnlinePayment;

public class WxPayUnifiedOrderRequestTypeJsApiPopulator
        implements
        MethodBasedWxPayUnifiedOrderRequestProviderSpecificRequestPopulator<WeChatPayJsApiSpecificOnlinePaymentRequest> {

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         WeChatPayJsApiSpecificOnlinePaymentRequest providerSpecificRequest) {
        request.setOpenid(providerSpecificRequest.getOpenId());
    }

    @Override
    public boolean supports(OnlinePayment.Method method) {
        return OnlinePayment.Method.of("WECHAT_PAY_JSAPI").equals(method);
    }
}
