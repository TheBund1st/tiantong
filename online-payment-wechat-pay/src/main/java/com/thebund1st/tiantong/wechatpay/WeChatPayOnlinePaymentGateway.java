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
        req.setBody("欢迎订购");//FIXME should extract from OnlinePayment
        req.setProductId(op.getId().getValue());
        req.setOutTradeNo(op.getId().getValue());
        req.setTotalFee(BigDecimal.valueOf(op.getAmount() * 100).intValue());
        req.setTradeType("NATIVE");//TODO should extract from OnlinePayment or dedicated gateway method
        req.setSpbillCreateIp(ipAddressExtractor.getLocalhostAddress());
        req.setNotifyUrl(webhookEndpoint);
        req.setNonceStr(nonceGenerator.next());
        WxPayNativeOrderResult result = this.wxPayService.createOrder(req);
        WeChatPayOrderResponse res = new WeChatPayOrderResponse();
        res.setQrCodeUri(result.getCodeUrl());
        return res;
    }


}
