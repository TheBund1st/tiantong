package com.thebund1st.tiantong.wechatpay.jsapi;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.method.Method;
import com.thebund1st.tiantong.time.Clock;
import com.thebund1st.tiantong.wechatpay.payment.MethodBasedWeChatPayLaunchOnlinePaymentRequestAssembler;
import com.thebund1st.tiantong.wechatpay.NonceGenerator;
import lombok.RequiredArgsConstructor;

import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayJsApi;


@RequiredArgsConstructor
public class WeChatPayJsApiLaunchOnlinePaymentRequestAssembler
        implements MethodBasedWeChatPayLaunchOnlinePaymentRequestAssembler
        <WeChatPayJsApiLaunchOnlinePaymentRequest> {
    private final NonceGenerator nonceGenerator;
    private final Clock clock;
    private final WxPayService wxPayService;

    @Override
    public boolean supports(Method method) {
        return weChatPayJsApi().equals(method);
    }

    @Override
    public WeChatPayJsApiLaunchOnlinePaymentRequest from(OnlinePayment onlinePayment, WxPayUnifiedOrderResult result) {
        WeChatPayJsApiLaunchOnlinePaymentRequest returning = new WeChatPayJsApiLaunchOnlinePaymentRequest();
        returning.setAppId(result.getAppid());
        returning.setTimestamp(String.valueOf(clock.epochSecond()));
        returning.setNonceStr(nonceGenerator.next());
        returning.setPayload("prepay_id=" + result.getPrepayId());
        returning.setSignType("MD5");
        returning.setPaySign(SignUtils.createSign(returning, "MD5",
                wxPayService.getConfig().getMchKey(), new String[]{}));
        return returning;
    }
}
