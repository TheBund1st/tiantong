package com.thebund1st.tiantong.wechatpay.webhooks


import com.thebund1st.tiantong.web.AbstractWebMvcTest
import spock.lang.Ignore

import static com.thebund1st.tiantong.commands.OnlinePaymentNotificationFixture.anOnlinePaymentNotification
import static com.thebund1st.tiantong.commands.OnlineRefundNotificationFixture.anOnlineRefundNotification
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath

class WeChatPayWebhookEndpointTest extends AbstractWebMvcTest {

    def "it should accept payment result notification from wechat pay"() {
        given:
        String xml = """
                    <xml>
                      <openid><![CDATA[sandboxopenid]]></openid>
                      <trade_type><![CDATA[NATIVE]]></trade_type>
                      <cash_fee_type><![CDATA[CNY]]></cash_fee_type>
                      <nonce_str><![CDATA[5a0e917058024362bffd8cb48cea6c8d]]></nonce_str>
                      <time_end><![CDATA[20190409230033]]></time_end>
                      <err_code_des><![CDATA[SUCCESS]]></err_code_des>
                      <return_code><![CDATA[SUCCESS]]></return_code>
                      <mch_id><![CDATA[mchId]]></mch_id>
                      <settlement_total_fee><![CDATA[301]]></settlement_total_fee>
                      <sign><![CDATA[4F26B514DFCEEC802D4886F498AD3189]]></sign>
                      <cash_fee><![CDATA[301]]></cash_fee>
                      <is_subscribe><![CDATA[Y]]></is_subscribe>
                      <return_msg><![CDATA[OK]]></return_msg>
                      <fee_type><![CDATA[CNY]]></fee_type>
                      <bank_type><![CDATA[CMC]]></bank_type>
                      <attach><![CDATA[sandbox_attach]]></attach>
                      <device_info><![CDATA[sandbox]]></device_info>
                      <out_trade_no><![CDATA[2763d14ba43747b894911acafdc70358]]></out_trade_no>
                      <result_code><![CDATA[SUCCESS]]></result_code>
                      <total_fee><![CDATA[301]]></total_fee>
                      <appid><![CDATA[appid]]></appid>
                      <transaction_id><![CDATA[4173181978320190409230033133868]]></transaction_id>
                      <err_code><![CDATA[SUCCESS]]></err_code>
                    </xml>
                """

        and:
        def command = anOnlinePaymentNotification()
                .sendTo(anOnlinePayment().idIs("2763d14ba43747b894911acafdc70358").build())
                .amountIs(3.01)
                .succeed()
                .text(xml)
                .build()
        weChatPayNotifyPaymentResultCommandAssembler.from(xml) >> command


        when:
        def resultActions = mockMvc.perform(post("/webhook/wechatpay/payment")
                .content(xml))

        then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(xpath("/xml/return_code").string("SUCCESS"))
                .andExpect(xpath("/xml/return_msg").string("OK"))
        and:
        this.onlinePaymentNotificationSubscriber.handle(command)
    }

    @Ignore
    def "it should accept refund result notification from wechat pay"() {
        given:
        String xml = """
                    <xml>
                        <return_code>SUCCESS</return_code>
                        <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
                        <mch_id><![CDATA[10000100]]></mch_id>
                        <nonce_str><![CDATA[TeqClE3i0mvn3DrK]]></nonce_str>
                        <req_info><![CDATA[T87GAHG17TGAHG1TGHAHAHA1Y1CIOA9UGJH1GAHV871HAGAGQYQQPOOJMXNBCXBVNMNMAJAA]]></req_info>
                    </xml>
                """

        and:
        def command = anOnlineRefundNotification()
                .succeed()
                .text(xml)
                .build()
        weChatPayNotifyPaymentResultCommandAssembler.from(xml) >> command


        when:
        def resultActions = mockMvc.perform(post("/webhook/wechatpay/payment")
                .content(xml))

        then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(xpath("/xml/return_code").string("SUCCESS"))
                .andExpect(xpath("/xml/return_msg").string("OK"))
        and:
        this.onlinePaymentNotificationSubscriber.handle(command)
    }
}
