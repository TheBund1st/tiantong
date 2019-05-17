package com.thebund1st.tiantong.wechatpay.qrcode;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.method.Method;
import com.thebund1st.tiantong.wechatpay.payment.MethodBasedWeChatPayLaunchOnlinePaymentRequestAssembler;
import lombok.RequiredArgsConstructor;

import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayNative;


@RequiredArgsConstructor
public class WeChatPayNativeLaunchOnlinePaymentRequestAssembler
        implements MethodBasedWeChatPayLaunchOnlinePaymentRequestAssembler
        <WeChatPayNativeLaunchOnlinePaymentRequest> {

    @Override
    public boolean supports(Method method) {
        return weChatPayNative().equals(method);
    }

    @Override
    public WeChatPayNativeLaunchOnlinePaymentRequest from(OnlinePayment onlinePayment, WxPayUnifiedOrderResult result) {
        WeChatPayNativeLaunchOnlinePaymentRequest returning = new WeChatPayNativeLaunchOnlinePaymentRequest();
        returning.setCodeUrl(result.getCodeURL());
        return returning;
    }
}
