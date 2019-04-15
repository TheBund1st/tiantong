package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jayway.jsonpath.JsonPath;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlineRefundProviderGateway;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentProviderGateway;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;

@RequiredArgsConstructor
public class WeChatPayOnlinePaymentGateway implements
        MethodBasedOnlinePaymentProviderGateway, OnlineRefundProviderGateway {

    private final WxPayService wxPayService;
    private final NonceGenerator nonceGenerator;
    private final IpAddressExtractor ipAddressExtractor;
    private final String webhookEndpoint;
    private final String notifyRefundResultWebhookEndpoint;

    @Override
    public ProviderSpecificRequest request(OnlinePayment onlinePayment) {
        WxPayUnifiedOrderResult response = requestPayment(onlinePayment);
        return new WeChatPaySpecificRequest(response);
    }

    @SneakyThrows
    public WxPayUnifiedOrderResult requestPayment(OnlinePayment op) {
        WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest();
        req.setAppid(wxPayService.getConfig().getAppId());
        req.setMchId(wxPayService.getConfig().getMchId());
        req.setBody(op.getSubject());
        if (op.getMethod().equals(OnlinePayment.Method.of("WECHAT_PAY_NATIVE"))) {
            req.setProductId(extractProductId(op.getProviderSpecificInfo()));
        } else if (op.getMethod().equals(OnlinePayment.Method.of("WECHAT_PAY_JSAPI"))) {
            req.setOpenid(extractOpenId(op.getProviderSpecificInfo()));
        }
        req.setOutTradeNo(op.getId().getValue()); // TODO maybe exposing id to public is not a good idea
        req.setTotalFee(toWeChatPayAmount(op.getAmount()));
        req.setTradeType(op.getMethod().getValue().replace("WECHAT_PAY_", ""));//TODO should extract from OnlinePayment or dedicated gateway method
        req.setSpbillCreateIp(ipAddressExtractor.getLocalhostAddress());
        req.setNotifyUrl(webhookEndpoint);
        req.setNonceStr(nonceGenerator.next());
        return this.wxPayService.unifiedOrder(req);
    }

    private int toWeChatPayAmount(double amount) {
        return BigDecimal.valueOf(amount * 100).intValue();
    }

    private String extractProductId(String providerSpecificInfo) {
        return JsonPath.read(providerSpecificInfo, "$.productId");
    }

    private String extractOpenId(String providerSpecificInfo) {
        return JsonPath.read(providerSpecificInfo, "$.openId");
    }

    @Override
    public List<OnlinePayment.Method> matchedMethods() {
        return asList(
                OnlinePayment.Method.of("WECHAT_PAY_NATIVE"),
                OnlinePayment.Method.of("WECHAT_PAY_JSAPI")
        );
    }

    @SneakyThrows
    @Override
    public void request(OnlineRefund onlineRefund) {
        WxPayRefundRequest req = new WxPayRefundRequest();
        req.setOutRefundNo(onlineRefund.getId().getValue());
        req.setOutTradeNo(onlineRefund.getOnlinePaymentId().getValue());
        req.setTotalFee(toWeChatPayAmount(onlineRefund.getOnlinePaymentAmount()));
        req.setRefundFee(toWeChatPayAmount(onlineRefund.getAmount()));
        req.setNonceStr(nonceGenerator.next());
        req.setNotifyUrl(notifyRefundResultWebhookEndpoint);
        this.wxPayService.refund(req);
    }
}
