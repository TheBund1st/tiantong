package com.thebund1st.tiantong.wechatpay.qrcode;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.wechatpay.payment.MethodBasedWeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator;

import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayNative;

public class WeChatPayNativeCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator
        implements
        MethodBasedWeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator<WeChatPayNativeCreateOnlinePaymentRequest> {

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         WeChatPayNativeCreateOnlinePaymentRequest providerSpecificRequest) {
        request.setProductId(providerSpecificRequest.getProductId());
    }

    @Override
    public boolean supports(OnlinePayment.Method method) {
        return weChatPayNative().equals(method);
    }
}
