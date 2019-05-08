package com.thebund1st.tiantong.wechatpay

import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand
import com.thebund1st.tiantong.json.AbstractJsonTest
import org.springframework.boot.test.json.JacksonTester

class WeChatPaySpecificOnlinePaymentRequestJsonTest extends AbstractJsonTest {

    private JacksonTester<RequestOnlinePaymentCommand> json

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
        RequestOnlinePaymentCommand actual = this.json.readObject(new ByteArrayInputStream(body.getBytes()))

        then:
        assert actual.providerSpecificRequest instanceof WeChatPayNativeSpecificOnlinePaymentRequest
        WeChatPayNativeSpecificOnlinePaymentRequest request =
                (WeChatPayNativeSpecificOnlinePaymentRequest) actual.providerSpecificRequest
        assert request.productId == "foo"
    }
}
