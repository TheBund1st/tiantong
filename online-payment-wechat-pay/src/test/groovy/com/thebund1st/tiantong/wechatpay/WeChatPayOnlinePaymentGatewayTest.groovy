package com.thebund1st.tiantong.wechatpay

import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest
import com.github.binarywang.wxpay.config.WxPayConfig
import com.github.binarywang.wxpay.service.WxPayService
import spock.lang.Specification

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class WeChatPayOnlinePaymentGatewayTest extends Specification {

    WeChatPayOnlinePaymentGateway subject
    WxPayService wxPayService = Mock()
    NonceGenerator nonceGenerator = Mock()
    IpAddressExtractor ipAddressExtractor = Mock()

    WxPayConfig config = new WxPayConfig()

    def setup() {
        subject = new WeChatPayOnlinePaymentGateway(wxPayService,
                nonceGenerator, ipAddressExtractor, "https://yourdomain.com/webhooks/wechatpay")
        wxPayService.getConfig() >> this.config
        config.setAppId("this_is_app_id")
        config.setMchId("this_is_merchant_id")
    }

    def "it should create unified order"() {
        given:
        def op = anOnlinePayment().amountIs(100).build()
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
        request.setNotifyUrl("https://yourdomain.com/webhooks/wechatpay");
        request.setNonceStr(nonce)
        def response = new WxPayNativeOrderResult()
        response.setCodeUrl("weixin://wxpay/bizpayurl?pr=lVQV8uF")
        and:
        nonceGenerator.next() >> nonce
        ipAddressExtractor.getLocalhostAddress() >> ipAddress
        wxPayService.createOrder(request) >> response

        when:
        WeChatPayOrderResponse actual = subject.requestPayment(op)

        then:
        assert actual.qrCodeUri == 'weixin://wxpay/bizpayurl?pr=lVQV8uF'
    }

}
