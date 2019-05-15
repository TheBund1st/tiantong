package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.OnlinePayment;

public class WxPayNativeUnifiedOrderRequestTypeNativePopulator
        implements
        MethodBasedWxPayUnifiedOrderRequestProviderSpecificRequestPopulator<WeChatPayNativeSpecificOnlinePaymentRequest> {

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         WeChatPayNativeSpecificOnlinePaymentRequest providerSpecificRequest) {
        request.setProductId(providerSpecificRequest.getProductId());
    }

    @Override
    public boolean supports(OnlinePayment.Method method) {
        return OnlinePayment.Method.of("WECHAT_PAY_NATIVE").equals(method);
    }
}
