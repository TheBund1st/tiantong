package com.thebund1st.tiantong.wechatpay.payment;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;

public interface WeChatPayLaunchOnlinePaymentRequestAssembler
        <P extends ProviderSpecificLaunchOnlinePaymentRequest> {

    P from(OnlinePayment onlinePayment, WxPayUnifiedOrderResult result);
}
