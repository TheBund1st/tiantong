package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;

public interface WxPayUnifiedOrderRequestProviderSpecificRequestPopulator
        <P extends ProviderSpecificOnlinePaymentRequest> {

    void populate(WxPayUnifiedOrderRequest request, P providerSpecificRequest);
}
