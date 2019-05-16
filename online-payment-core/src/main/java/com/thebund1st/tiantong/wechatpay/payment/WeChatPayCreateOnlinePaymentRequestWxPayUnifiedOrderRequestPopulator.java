package com.thebund1st.tiantong.wechatpay.payment;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;

public interface WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator
        <P extends ProviderSpecificCreateOnlinePaymentRequest> {

    void populate(WxPayUnifiedOrderRequest request, P providerSpecificRequest);
}
