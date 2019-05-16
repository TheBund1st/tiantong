package com.thebund1st.tiantong.boot.provider


import com.thebund1st.tiantong.boot.AbstractAutoConfigurationTest
import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentGateway
import com.thebund1st.tiantong.provider.CreateOnlinePaymentProviderGatewayDispatcher
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway

import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayJsApi
import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayNative

class ProviderConfigurationTest extends AbstractAutoConfigurationTest {

    def "it should provide a RequestOnlinePaymentCommandHandler instance"() {

        when:
        def contextRunner = this.contextRunner

        then:
        contextRunner.run { it ->
            CreateOnlinePaymentProviderGatewayDispatcher dispatcher =
                    (CreateOnlinePaymentProviderGatewayDispatcher) it.getBean(ProviderSpecificCreateOnlinePaymentGateway)

            WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway =
                    (WeChatPayOnlinePaymentGateway) it.getBean(WeChatPayOnlinePaymentGateway)

            assert dispatcher.gatewayGroup
                    .find { it.supports(weChatPayNative()) } ==
                    weChatPayOnlinePaymentGateway
            assert dispatcher.gatewayGroup
                    .find { it.supports(weChatPayJsApi()) } ==
                    weChatPayOnlinePaymentGateway
        }
    }
}
