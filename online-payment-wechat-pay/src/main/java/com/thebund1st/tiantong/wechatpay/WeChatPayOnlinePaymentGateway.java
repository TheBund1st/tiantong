package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jayway.jsonpath.JsonPath;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import com.thebund1st.tiantong.web.rest.resources.OnlinePaymentResource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class WeChatPayOnlinePaymentGateway implements OnlinePaymentProviderGateway {

    private final WxPayService wxPayService;
    private final NonceGenerator nonceGenerator;
    private final IpAddressExtractor ipAddressExtractor;
    private final String webhookEndpoint;

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
        req.setTotalFee(BigDecimal.valueOf(op.getAmount() * 100).intValue());
        req.setTradeType(op.getMethod().getValue().replace("WECHAT_PAY_", ""));//TODO should extract from OnlinePayment or dedicated gateway method
        req.setSpbillCreateIp(ipAddressExtractor.getLocalhostAddress());
        req.setNotifyUrl(webhookEndpoint);
        req.setNonceStr(nonceGenerator.next());
        return this.wxPayService.unifiedOrder(req);
    }

    private String extractProductId(String providerSpecificInfo) {
        return JsonPath.read(providerSpecificInfo, "$.productId");
    }

    private String extractOpenId(String providerSpecificInfo) {
        return JsonPath.read(providerSpecificInfo, "$.openId");
    }

}
