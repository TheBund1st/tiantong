package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.ProviderSpecificUserAgentOnlinePaymentRequest;

public interface WeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler
        <P extends ProviderSpecificUserAgentOnlinePaymentRequest> {

    P from(OnlinePayment onlinePayment, WxPayUnifiedOrderResult result);
}
