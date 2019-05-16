package com.thebund1st.tiantong.wechatpay

import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult
import com.github.binarywang.wxpay.config.WxPayConfig
import com.github.binarywang.wxpay.service.WxPayService
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest
import com.thebund1st.tiantong.time.Clock
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiCreateOnlinePaymentRequest
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiLaunchOnlinePaymentRequest
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiLaunchOnlinePaymentRequestAssembler
import com.thebund1st.tiantong.wechatpay.payment.WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulatorDispatcher
import com.thebund1st.tiantong.wechatpay.payment.WeChatPayLaunchOnlinePaymentRequestAssemblerDispatcher
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeCreateOnlinePaymentRequest
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeLaunchOnlinePaymentRequest
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeLaunchOnlinePaymentRequestAssembler
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.OnlinePaymentResultFixture.anOnlinePaymentResult
import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund
import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayJsApi
import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayNative

class WeChatPayOnlinePaymentProviderGatewayTest extends Specification {

    WeChatPayOnlinePaymentGateway subject
    WxPayService wxPayService = Mock()
    NonceGenerator nonceGenerator = Mock()
    IpAddressExtractor ipAddressExtractor = Mock()
    Clock clock = Mock()
    OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResultNotificationIdentifierGenerator = Mock()
    WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulatorDispatcher<? extends ProviderSpecificCreateOnlinePaymentRequest> dispatcher =
            new WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulatorDispatcher([
                    new WeChatPayNativeCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator(),
                    new WeChatPayJsApiCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator()
            ])
    WeChatPayLaunchOnlinePaymentRequestAssemblerDispatcher weChatPayProviderSpecificUserAgentRequestAssembler =
            new WeChatPayLaunchOnlinePaymentRequestAssemblerDispatcher([
                    new WeChatPayNativeLaunchOnlinePaymentRequestAssembler(),
                    new WeChatPayJsApiLaunchOnlinePaymentRequestAssembler(nonceGenerator, clock, wxPayService),
            ])

    WxPayConfig config = new WxPayConfig()
    private ipAddress = "172.23.231.22"
    private nonce = "this_is_a_unique_str"
    private long epochSecond = ZonedDateTime.now().toEpochSecond()
    private LocalDateTime now = LocalDateTime.now()

    def setup() {
        subject = new WeChatPayOnlinePaymentGateway(wxPayService,
                nonceGenerator, ipAddressExtractor,
                "https://yourdomain.com/webhooks/wechatpay",
                "https://yourdomain.com/webhooks/wechatpay/refund",
                dispatcher,
                weChatPayProviderSpecificUserAgentRequestAssembler,
                onlinePaymentResultNotificationIdentifierGenerator,
                clock)
        wxPayService.getConfig() >> this.config
        config.setAppId("this_is_app_id")
        config.setMchId("this_is_merchant_id")

        clock.epochSecond() >> epochSecond

        clock.now() >> now
    }

    def "it should create unified order for jsapi"() {
        given:
        def openId = "This is openId"
        def op = anOnlinePayment().amountIs(100)
                .withOpenId(openId)
                .by(weChatPayJsApi())
                .build()
        def request = new WxPayUnifiedOrderRequest()
        request.setAppid("this_is_app_id")
        request.setMchId("this_is_merchant_id")
        request.setBody(op.subject)
        request.setOutTradeNo(op.id.value)
        request.setTotalFee(10000)
        request.setTradeType("JSAPI")
        request.setSpbillCreateIp(this.ipAddress)
        request.setNotifyUrl("https://yourdomain.com/webhooks/wechatpay")
        request.setNonceStr(this.nonce)
        request.setOpenid(openId)
        request.setTimeStart(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(op.createdAt))
        request.setTimeExpire(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(op.expiresAt))
        def response = new WxPayUnifiedOrderResult()
        response.setCodeURL("weixin://wxpay/bizpayurl?pr=lVQV8uF")
        response.setPrepayId("this_is_prepay_id")
        response.setAppid(config.getAppId())
        and:
        nonceGenerator.next() >> this.nonce
        ipAddressExtractor.getLocalhostAddress() >> this.ipAddress
        wxPayService.unifiedOrder(request) >> response

        when:
        def actual = subject.create(op, new WeChatPayJsApiCreateOnlinePaymentRequest(openId: openId))

        then:
        def payResult = (WeChatPayJsApiLaunchOnlinePaymentRequest) actual

        assert payResult.getAppId() == this.config.getAppId()
        assert payResult.getTimestamp() == "${this.epochSecond}"
        assert payResult.getNonceStr() == this.nonce
        assert payResult.getPayload() == "prepay_id=${response.getPrepayId()}"
        assert payResult.getSignType() == "MD5"
        assert payResult.getPaySign()
    }

    def "it should create unified order for native"() {
        given:
        def productId = "This is productId"
        def op = anOnlinePayment().amountIs(100)
                .withProductId(productId)
                .by(weChatPayNative())
                .build()
        def nonce = "this_is_a_unique_str"
        def ipAddress = "172.23.231.22"
        def request = new WxPayUnifiedOrderRequest()
        request.setAppid("this_is_app_id")
        request.setMchId("this_is_merchant_id")
        request.setBody(op.subject)
        request.setOutTradeNo(op.id.value)
        request.setTotalFee(10000)
        request.setTradeType("NATIVE")
        request.setSpbillCreateIp(ipAddress)
        request.setNotifyUrl("https://yourdomain.com/webhooks/wechatpay")
        request.setNonceStr(nonce)
        request.setProductId(productId)
        request.setTimeStart(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(op.createdAt))
        request.setTimeExpire(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(op.expiresAt))
        def response = new WxPayUnifiedOrderResult()
        response.setCodeURL("weixin://wxpay/bizpayurl?pr=lVQV8uF")
        and:
        nonceGenerator.next() >> nonce
        ipAddressExtractor.getLocalhostAddress() >> ipAddress
        wxPayService.unifiedOrder(request) >> response

        when:
        def actual = subject.create(op, new WeChatPayNativeCreateOnlinePaymentRequest(productId: productId))


        then:
        def payResult = (WeChatPayNativeLaunchOnlinePaymentRequest) actual
        assert payResult.getCodeUrl() == 'weixin://wxpay/bizpayurl?pr=lVQV8uF'
    }

    def "it should query unified order"() {
        given:
        def productId = "This is productId"
        def op = anOnlinePayment().amountIs(100)
                .withProductId(productId)
                .by(weChatPayNative())
                .build()
        def nonce = "this_is_a_unique_str"
        def request = new WxPayOrderQueryRequest()
        request.setAppid("this_is_app_id")
        request.setMchId("this_is_merchant_id")
        request.setOutTradeNo(op.id.value)
        request.setNonceStr(nonce)
        def response = new WxPayOrderQueryResult()
        response.setOutTradeNo(op.id.value)
        response.setTotalFee((op.amount * 100).intValue())
        response.setTradeState("SUCCESS")
        def paymentResult = anOnlinePaymentResult().sendTo(op).build()
        def now = LocalDateTime.now()

        and:
        nonceGenerator.next() >> nonce
        wxPayService.queryOrder(request) >> response
        onlinePaymentResultNotificationIdentifierGenerator.nextIdentifier() >> paymentResult.id
        clock.now() >> now

        when:
        def actual = subject.pull(op)


        then:
        assert actual.isPresent()
        actual.ifPresent {
            assert it.id == paymentResult.id
            assert it.onlinePaymentId == op.id
            assert it.amount == op.amount
            assert it.text == response.getXmlString()
            assert it.createdAt
        }
    }

    def "it should return empty when query unified order given the result is NOT PAY"() {
        given:
        def productId = "This is productId"
        def op = anOnlinePayment().amountIs(100)
                .withProductId(productId)
                .by(weChatPayNative())
                .build()
        def nonce = "this_is_a_unique_str"
        def request = new WxPayOrderQueryRequest()
        request.setAppid("this_is_app_id")
        request.setMchId("this_is_merchant_id")
        request.setOutTradeNo(op.id.value)
        request.setNonceStr(nonce)
        def response = new WxPayOrderQueryResult()
        response.setOutTradeNo(op.id.value)
        response.setTotalFee((op.amount * 100).intValue())
        response.setTradeState("NOTPAY")

        and:
        nonceGenerator.next() >> nonce
        wxPayService.queryOrder(request) >> response

        when:
        def actual = subject.pull(op)


        then:
        assert !actual.isPresent()
    }

    def "it should request refund"() {
        given:
        def or = anOnlineRefund()
                .with(anOnlinePayment().amountIs(100))
                .build()
        def request = new WxPayRefundRequest()
        request.setOutRefundNo(or.id.value)
        request.setOutTradeNo(or.onlinePaymentId.value)
        request.setTotalFee(10000)
        request.setRefundFee(10000)
        request.setNotifyUrl("https://yourdomain.com/webhooks/wechatpay/refund")
        request.setNonceStr(nonce)

        and:
        nonceGenerator.next() >> nonce

        when:
        subject.request(or)

        then:
        1 * wxPayService.refund(request)
    }

}
