package com.thebund1st.tiantong.wechatpay

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult
import com.github.binarywang.wxpay.config.WxPayConfig
import com.github.binarywang.wxpay.service.WxPayService
import com.thebund1st.tiantong.core.EmptyOnlinePaymentRequest
import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest
import spock.lang.Specification

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund

class WeChatPayOnlinePaymentProviderGatewayTest extends Specification {

    WeChatPayOnlinePaymentGateway subject
    WxPayService wxPayService = Mock()
    NonceGenerator nonceGenerator = Mock()
    IpAddressExtractor ipAddressExtractor = Mock()
    WxPayUnifiedOrderRequestProviderSpecificRequestPopulatorDispatcher<? extends ProviderSpecificOnlinePaymentRequest> dispatcher =
            new WxPayUnifiedOrderRequestProviderSpecificRequestPopulatorDispatcher([new WxPayNativeUnifiedOrderRequestTypeNativePopulator()])

    WxPayConfig config = new WxPayConfig()
    private ipAddress = "172.23.231.22"
    private nonce = "this_is_a_unique_str"

    def setup() {
        subject = new WeChatPayOnlinePaymentGateway(wxPayService,
                nonceGenerator, ipAddressExtractor,
                "https://yourdomain.com/webhooks/wechatpay",
                "https://yourdomain.com/webhooks/wechatpay/refund",
                dispatcher)
        wxPayService.getConfig() >> this.config
        config.setAppId("this_is_app_id")
        config.setMchId("this_is_merchant_id")
    }

    def "it should create unified order for jsapi"() {
        given:
        def op = anOnlinePayment().amountIs(100)
                .withOpenId("This is openId")
                .by(OnlinePayment.Method.of("WECHAT_PAY_JSAPI"))
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
        request.setOpenid("This is openId")
        def response = new WxPayUnifiedOrderResult()
        response.setCodeURL("weixin://wxpay/bizpayurl?pr=lVQV8uF")
        and:
        nonceGenerator.next() >> this.nonce
        ipAddressExtractor.getLocalhostAddress() >> this.ipAddress
        wxPayService.unifiedOrder(request) >> response

        when:
        def actual = subject.request(op, new EmptyOnlinePaymentRequest())

        then:
        def payResult = (WeChatPaySpecificRequest) actual

        assert payResult.getCodeUrl() == 'weixin://wxpay/bizpayurl?pr=lVQV8uF'
    }

    def "it should create unified order for native"() {
        given:
        def productId = "This is productId"
        def op = anOnlinePayment().amountIs(100)
                .withProductId(productId)
                .by(OnlinePayment.Method.of("WECHAT_PAY_NATIVE"))
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
        def response = new WxPayUnifiedOrderResult()
        response.setCodeURL("weixin://wxpay/bizpayurl?pr=lVQV8uF")
        and:
        nonceGenerator.next() >> nonce
        ipAddressExtractor.getLocalhostAddress() >> ipAddress
        wxPayService.unifiedOrder(request) >> response

        when:
        def actual = subject.request(op, new WeChatPayNativeSpecificOnlinePaymentRequest(productId: productId))


        then:
        def payResult = (WeChatPaySpecificRequest) actual
        assert payResult.getCodeUrl() == 'weixin://wxpay/bizpayurl?pr=lVQV8uF'
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
