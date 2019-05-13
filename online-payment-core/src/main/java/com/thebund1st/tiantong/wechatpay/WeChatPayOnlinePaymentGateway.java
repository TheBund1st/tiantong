package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlineRefundProviderGateway;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import com.thebund1st.tiantong.core.ProviderSpecificUserAgentOnlinePaymentRequest;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentProviderGateway;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentResultGateway;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@RequiredArgsConstructor
public class WeChatPayOnlinePaymentGateway implements
        MethodBasedOnlinePaymentProviderGateway,
        MethodBasedOnlinePaymentResultGateway,
        OnlineRefundProviderGateway {

    private final WxPayService wxPayService;
    private final NonceGenerator nonceGenerator;
    private final IpAddressExtractor ipAddressExtractor;
    private final String webhookEndpoint;
    private final String notifyRefundResultWebhookEndpoint;
    private final WxPayUnifiedOrderRequestProviderSpecificRequestPopulator<ProviderSpecificOnlinePaymentRequest>
            wxPayUnifiedOrderRequestProviderSpecificRequestPopulator;
    private final WeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler weChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler;
    private final OnlinePaymentResultNotificationIdentifierGenerator notificationIdentifierGenerator;
    private final Clock clock;

    @Override
    public ProviderSpecificUserAgentOnlinePaymentRequest request(OnlinePayment onlinePayment,
                                                                 ProviderSpecificOnlinePaymentRequest providerSpecificRequest) {
        WxPayUnifiedOrderResult result = requestPayment(onlinePayment, providerSpecificRequest);
        return weChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler.from(onlinePayment, result);
    }

    @SneakyThrows
    public WxPayUnifiedOrderResult requestPayment(OnlinePayment op,
                                                  ProviderSpecificOnlinePaymentRequest providerSpecificRequest) {
        WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest();
        req.setAppid(wxPayService.getConfig().getAppId());
        req.setMchId(wxPayService.getConfig().getMchId());
        req.setBody(op.getSubject());
        req.setTradeType(op.getMethod().getValue().replace("WECHAT_PAY_", ""));//TODO should extract from OnlinePayment or dedicated gateway method
        req.setOutTradeNo(op.getId().getValue()); // TODO maybe exposing id to public is not a good idea
        req.setTotalFee(toWeChatPayAmount(op.getAmount()));
        req.setSpbillCreateIp(ipAddressExtractor.getLocalhostAddress());
        req.setNotifyUrl(webhookEndpoint);
        req.setNonceStr(nonceGenerator.next());
        wxPayUnifiedOrderRequestProviderSpecificRequestPopulator.populate(req, providerSpecificRequest);
        return this.wxPayService.unifiedOrder(req);
    }

    private int toWeChatPayAmount(double amount) {
        return BigDecimal.valueOf(amount * 100).intValue();
    }

    private List<OnlinePayment.Method> matchedMethods() {
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

    @SneakyThrows
    @Override
    public Optional<OnlinePaymentResultNotification> pull(OnlinePayment onlinePayment) {
        WxPayOrderQueryRequest req = new WxPayOrderQueryRequest();
        req.setAppid(wxPayService.getConfig().getAppId());
        req.setMchId(wxPayService.getConfig().getMchId());
        req.setNonceStr(nonceGenerator.next());
        req.setOutTradeNo(onlinePayment.getId().getValue());
        WxPayOrderQueryResult result = this.wxPayService.queryOrder(req);
        //TODO extract constant
        if ("SUCCESS".equals(result.getTradeState())) {
            OnlinePaymentResultNotification paymentResult = new OnlinePaymentResultNotification();
            paymentResult.setId(notificationIdentifierGenerator.nextIdentifier());
            paymentResult.setOnlinePaymentId(OnlinePayment.Identifier.of(result.getOutTradeNo()));
            paymentResult.setAmount(BigDecimal.valueOf(result.getTotalFee())
                    .divide(BigDecimal.valueOf(100)).doubleValue());
            paymentResult.setCode(OnlinePaymentResultNotification.Code.SUCCESS);
            paymentResult.setCreatedAt(clock.now());
            paymentResult.setText(result.getXmlString());
            return Optional.of(paymentResult);
        } else if ("NOT_PAY".equals(result.getTradeState())) {
            return Optional.empty();
        } else {
            //TODO handle "CLOSED"/"REFUND"/"PAYERROR" and others
            return Optional.empty();
        }

    }

    @Override
    public boolean supports(OnlinePayment.Method method) {
        return matchedMethods().contains(method);
    }
}
