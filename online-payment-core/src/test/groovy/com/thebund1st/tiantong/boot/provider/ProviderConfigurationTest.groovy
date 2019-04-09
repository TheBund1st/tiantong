package com.thebund1st.tiantong.boot.provider


import com.thebund1st.tiantong.boot.AbstractAutoConfigurationTest
import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway
import com.thebund1st.tiantong.provider.OnlinePaymentProviderGatewayDispatcher
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway

class ProviderConfigurationTest extends AbstractAutoConfigurationTest {

    def "it should provide a RequestOnlinePaymentCommandHandler instance"() {

        when:
        def contextRunner = this.contextRunner

        then:
        contextRunner.run { it ->
            OnlinePaymentProviderGatewayDispatcher dispatcher =
                    (OnlinePaymentProviderGatewayDispatcher) it.getBean(OnlinePaymentProviderGateway)

            WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway =
                    (WeChatPayOnlinePaymentGateway) it.getBean(WeChatPayOnlinePaymentGateway)

            assert dispatcher.delegates
                    .get(OnlinePayment.Method.of("WECHAT_PAY_NATIVE")) == weChatPayOnlinePaymentGateway
            assert dispatcher.delegates
                    .get(OnlinePayment.Method.of("WECHAT_PAY_JSAPI")) == weChatPayOnlinePaymentGateway
        }
    }
}
