package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class WeChatPayOnlinePaymentGateway {

    private final WxPayService wxPayService;
    private final NonceGenerator nonceGenerator;
    private final IpAddressExtractor ipAddressExtractor;
    private final String webhookEndpoint;

    @SneakyThrows
    public WeChatPayOrderResponse requestPayment(OnlinePayment op) {
        WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest();
        req.setAppid(wxPayService.getConfig().getAppId());
        req.setMchId(wxPayService.getConfig().getMchId());
        req.setBody(op.getSubject());
        if (op.getMethod().equals(OnlinePayment.Method.of("WECHAT_PAY_NATIVE"))) {
            req.setProductId(op.getProductId());
        } else if (op.getMethod().equals(OnlinePayment.Method.of("WECHAT_PAY_JSAPI"))) {
            req.setOpenid(op.getOpenId());
        }
        req.setOutTradeNo(op.getId().getValue()); // TODO maybe exposing id to public is not a good idea
        req.setTotalFee(BigDecimal.valueOf(op.getAmount() * 100).intValue());
        req.setTradeType(op.getMethod().getValue().replace("WECHAT_PAY_", ""));//TODO should extract from OnlinePayment or dedicated gateway method
        req.setSpbillCreateIp(ipAddressExtractor.getLocalhostAddress());
        req.setNotifyUrl(webhookEndpoint);
        req.setNonceStr(nonceGenerator.next());
        Object result = this.wxPayService.createOrder(req);
        WeChatPayOrderResponse res = new WeChatPayOrderResponse();
        if (result instanceof WxPayNativeOrderResult) {
            WxPayNativeOrderResult nativeOrder = (WxPayNativeOrderResult) result;
            res.setQrCodeUri(nativeOrder.getCodeUrl());
        }
        return res;
    }


}
