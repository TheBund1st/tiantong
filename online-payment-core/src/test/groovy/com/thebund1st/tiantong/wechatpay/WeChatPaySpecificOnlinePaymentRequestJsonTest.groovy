package com.thebund1st.tiantong.wechatpay

import com.thebund1st.tiantong.commands.CreateOnlinePaymentCommand
import com.thebund1st.tiantong.json.AbstractJsonTest
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiCreateOnlinePaymentRequest
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeCreateOnlinePaymentRequest
import org.springframework.boot.test.json.JacksonTester

class WeChatPaySpecificOnlinePaymentRequestJsonTest extends AbstractJsonTest {

    private JacksonTester<CreateOnlinePaymentCommand> json

    def "it should deserialize when request WeChat pay native"() {

        given:
        def body = """
            {
                "method": "WECHAT_PAY_NATIVE",
                "providerSpecificRequest": {
                    "product_id": "foo"
                }
            }
        """

        when:
        CreateOnlinePaymentCommand actual = this.json.readObject(new ByteArrayInputStream(body.getBytes()))

        then:
        assert actual.providerSpecificRequest instanceof WeChatPayNativeCreateOnlinePaymentRequest
        WeChatPayNativeCreateOnlinePaymentRequest request =
                (WeChatPayNativeCreateOnlinePaymentRequest) actual.providerSpecificRequest
        assert request.productId == "foo"
    }

    def "it should deserialize when request WeChat jsapi native"() {

        given:
        def body = """
            {
                "method": "WECHAT_PAY_JSAPI",
                "providerSpecificRequest": {
                    "openid": "foo"
                }
            }
        """

        when:
        CreateOnlinePaymentCommand actual = this.json.readObject(new ByteArrayInputStream(body.getBytes()))

        then:
        assert actual.providerSpecificRequest instanceof WeChatPayJsApiCreateOnlinePaymentRequest
        WeChatPayJsApiCreateOnlinePaymentRequest request =
                (WeChatPayJsApiCreateOnlinePaymentRequest) actual.providerSpecificRequest
        assert request.openId == "foo"
    }
}
