package com.thebund1st.tiantong.wechatpay

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult
import com.thebund1st.tiantong.json.AbstractJsonTest
import org.springframework.boot.test.json.JacksonTester

import static org.assertj.core.api.Java6Assertions.assertThat

class WeChatPaySpecificRequestJsonTest extends AbstractJsonTest {

    private JacksonTester<WeChatPaySpecificRequest> json

    def "it should serialize"() {


        given:
        WxPayUnifiedOrderResult result = new WxPayUnifiedOrderResult()
        result.codeURL = "https://www.codeurl.com"
        WeChatPaySpecificRequest request = new WeChatPaySpecificRequest(result)

        when:
        def content = this.json.write(request)

        then:
        assertThat(content)
                .extractingJsonPathStringValue("@.code_url").isEqualTo(request.codeUrl)
    }
}
