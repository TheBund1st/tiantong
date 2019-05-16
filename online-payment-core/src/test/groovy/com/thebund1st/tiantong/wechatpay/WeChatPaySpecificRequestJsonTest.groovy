package com.thebund1st.tiantong.wechatpay


import com.thebund1st.tiantong.json.AbstractJsonTest
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiLaunchOnlinePaymentRequest
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeLaunchOnlinePaymentRequest
import org.springframework.boot.test.json.JacksonTester

import static org.assertj.core.api.Java6Assertions.assertThat

class WeChatPaySpecificRequestJsonTest extends AbstractJsonTest {

    private JacksonTester<WeChatPayNativeLaunchOnlinePaymentRequest> nativeJson
    private JacksonTester<WeChatPayJsApiLaunchOnlinePaymentRequest> jsApiJson

    def "it should serialize WeChatPayNativeSpecificOnlinePaymentUserAgentRequest"() {
        given:
        WeChatPayNativeLaunchOnlinePaymentRequest request = new WeChatPayNativeLaunchOnlinePaymentRequest()
        request.setCodeUrl("https://www.codeurl.com")

        when:
        def content = this.nativeJson.write(request)

        then:
        assertThat(content)
                .extractingJsonPathStringValue("@.code_url").isEqualTo(request.codeUrl)
    }

    def "it should serialize WeChatPayJsApiSpecificOnlinePaymentUserAgentRequest"() {
        given:
        WeChatPayJsApiLaunchOnlinePaymentRequest request = new WeChatPayJsApiLaunchOnlinePaymentRequest()
        request.setAppId("appId")
        request.setTimestamp("timestamp")
        request.setPayload("package")
        request.setNonceStr("nonce")
        request.setSignType("signType")
        request.setPaySign("paySign")

        when:
        def content = this.jsApiJson.write(request)

        then:
        assertThat(content)
                .extractingJsonPathStringValue("@.appId").isEqualTo(request.appId)
        assertThat(content)
                .extractingJsonPathStringValue("@.timeStamp").isEqualTo(request.timestamp)
        assertThat(content)
                .extractingJsonPathStringValue("@.nonceStr").isEqualTo(request.nonceStr)
        assertThat(content)
                .extractingJsonPathStringValue("@.package").isEqualTo(request.payload)
        assertThat(content)
                .extractingJsonPathStringValue("@.signType").isEqualTo(request.signType)
        assertThat(content)
                .extractingJsonPathStringValue("@.paySign").isEqualTo(request.paySign)
    }
}
