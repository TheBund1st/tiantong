package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class WeChatPayJsApiSpecificUserAgentOnlinePaymentRequestAssembler
        implements MethodBasedWeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler
        <WeChatPayJsApiSpecificUserAgentOnlinePaymentRequest> {
    private final NonceGenerator nonceGenerator;
    private final Clock clock;
    private final WxPayService wxPayService;

    @Override
    public boolean supports(OnlinePayment.Method method) {
        return OnlinePayment.Method.of("WECHAT_PAY_JSAPI").equals(method);
    }

    @Override
    public WeChatPayJsApiSpecificUserAgentOnlinePaymentRequest from(OnlinePayment onlinePayment, WxPayUnifiedOrderResult result) {
        WeChatPayJsApiSpecificUserAgentOnlinePaymentRequest returning = new WeChatPayJsApiSpecificUserAgentOnlinePaymentRequest();
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
