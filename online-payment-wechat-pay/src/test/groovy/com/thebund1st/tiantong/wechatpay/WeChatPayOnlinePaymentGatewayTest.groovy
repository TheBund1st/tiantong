package com.thebund1st.tiantong.wechatpay

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import org.junit.Rule
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static org.springframework.http.HttpStatus.OK

class WeChatPayOnlinePaymentGatewayTest extends Specification {

    public static final int WIRE_MOCK_PORT = 17819

    @Rule
    public WireMockRule wm = new WireMockRule(wireMockConfig().port(WIRE_MOCK_PORT))

    private RestTemplate restTemplate = new RestTemplate()
    WeChatPayOnlinePaymentGateway subject = new WeChatPayOnlinePaymentGateway(restTemplate)

    def setup() {
        subject.setAppId("this_is_app_id")
        subject.setMerchantId("this_is_merchant_id")
        subject.setKey("this_is_key_keep_it_secret")
        subject.setWebhookEndpoint("https://yourdomain.com/webhooks/wechatpay")
        subject.setBaseUri("http://localhost:${wm.port()}")
    }

    def "it should create unified order"() {
        given:
        def op = anOnlinePayment().build()

        and:
        wm.stubFor(post(urlEqualTo("/pay/unifiedorder"))
                .withRequestBody(matchingXPath("//appid", equalToXml("<appid>this_is_app_id</appid>")))
                .withRequestBody(matchingXPath("//mch_id", equalToXml("<mch_id>this_is_merchant_id</mch_id>")))
//                .withRequestBody(matchingXPath("//body", equalToXml("<body>欢迎订购</body>"))) FIXME figure out why it doesn't work
                .withRequestBody(matchingXPath("//out_trade_no", equalToXml("<out_trade_no>${op.id.value}</out_trade_no>")))
                .withRequestBody(matchingXPath("//total_fee", equalToXml("<totel_fee>${String.valueOf(op.amount * 100)}</totel_fee>")))
                .withRequestBody(matchingXPath("//trade_type", equalToXml("<trade_type>NATIVE</trade_type>")))
                .withRequestBody(matchingXPath("//notify_url", equalToXml("<notify_url>https://yourdomain.com/webhooks/wechatpay</notify_url>")))
                .withRequestBody(matchingXPath("//nonce_str", new AnythingPattern())) //FIXME delegation
                .withRequestBody(matchingXPath("//spbill_create_ip", new AnythingPattern())) //FIXME delegation
                .withRequestBody(matchingXPath("//sign", new AnythingPattern()))//FIXME delegation
                .willReturn(aResponse().withStatus(OK.value())
                .withBody("""
                <xml>
                    <return_code><![CDATA[SUCCESS]]></return_code>
                    <return_msg><![CDATA[OK]]></return_msg>
                    <appid><![CDATA[this_is_app_id]]></appid>
                    <mch_id><![CDATA[this_is_mch_id]]></mch_id>
                    <nonce_str><![CDATA[9RZILsz4KxpFq804]]></nonce_str>
                    <sign><![CDATA[DF3ABEF3384F6B206C860F7666881E80]]></sign>
                    <result_code><![CDATA[SUCCESS]]></result_code>
                    <prepay_id><![CDATA[this_is_prepay_id]]></prepay_id>
                    <trade_type><![CDATA[NATIVE]]></trade_type>
                    <code_url><![CDATA[weixin://wxpay/bizpayurl?pr=lVQV8uF]]></code_url>
                </xml>
        """)))

        when:
        def actual = subject.requestPayment(op)

        then:
        assert actual.qrCodeUri == 'weixin://wxpay/bizpayurl?pr=lVQV8uF'
    }

}
