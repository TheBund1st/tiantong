package com.thebund1st.tiantong.boot.wechatpay

import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl
import com.thebund1st.tiantong.boot.AbstractAutoConfigurationTest
import com.thebund1st.tiantong.wechatpay.IpAddressExtractor
import com.thebund1st.tiantong.wechatpay.NonceGenerator
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyOnlinePaymentResultCommandAssembler
import spock.lang.Ignore

class WeChatPayConfigurationTest extends AbstractAutoConfigurationTest {

    def "it should provide a NonceGenerator instance"() {

        when:
        def contextRunner = this.contextRunner

        then:
        contextRunner.run { it ->
            NonceGenerator actual = it.getBean(NonceGenerator)
            assert actual != null
        }
    }

    def "it should provide a IpAddressExtractor instance"() {

        when:
        def contextRunner = this.contextRunner

        then:
        contextRunner.run { it ->
            IpAddressExtractor actual = it.getBean(IpAddressExtractor)
            assert actual != null
        }
    }

    //FIXME
    @Ignore("TODO")
    def "it should provide a WeChatPayProperties instance"() {

        when:
        def then = this.contextRunner
                .withPropertyValues(
                "tiantong.wechatpay.appId=appId",
                "tiantong.wechatpay.merchantId=merchantId",
                "tiantong.wechatpay.merchantKey=merchantKey",
                "tiantong.wechatpay.sandbox=true")

        then:
        then.run {
            WeChatPayProperties actual = it.getBean(WeChatPayProperties)
            assert actual.appId == 'appId'
            assert actual.merchantId == 'merchantId'
            assert actual.merchantKey == 'merchantKey'
            assert actual.sandbox
        }
    }

    //FIXME
    @Ignore("TODO")
    def "it should provide a WxPayServiceImpl instance"() {

        when:
        def then = this.contextRunner
                .withPropertyValues(
                "tiantong.wechatpay.appId=appId",
                "tiantong.wechatpay.merchantId=merchantId",
                "tiantong.wechatpay.merchantKey=merchantKey",
                "tiantong.wechatpay.sandbox=true")

        then:
        then.run {
            WxPayServiceImpl actual = it.getBean(WxPayServiceImpl)
            assert actual.getConfig().appId == 'appId'
            assert actual.getConfig().mchId == 'merchantId'
            assert actual.getConfig().mchKey == 'merchantKey'
            assert actual.getConfig().useSandboxEnv
        }
    }

    def "it should provide a WeChatPayOnlinePaymentGateway instance"() {

        when:
        def then = this.contextRunner

        then:
        then.run {
            WeChatPayOnlinePaymentGateway actual = it.getBean(WeChatPayOnlinePaymentGateway)
            assert actual != null
        }
    }

    def "it should provide a WeChatPayNotifyPaymentResultCommandAssembler instance"() {

        when:
        def then = this.contextRunner

        then:
        then.run {
            WeChatPayNotifyOnlinePaymentResultCommandAssembler actual = it.getBean(WeChatPayNotifyOnlinePaymentResultCommandAssembler)
            assert actual != null
        }
    }

}
