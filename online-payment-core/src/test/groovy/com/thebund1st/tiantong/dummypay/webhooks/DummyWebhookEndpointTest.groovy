package com.thebund1st.tiantong.dummypay.webhooks

import com.thebund1st.tiantong.web.AbstractWebMvcTest

import static com.thebund1st.tiantong.commands.OnlinePaymentNotificationFixture.anOnlinePaymentNotification
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static org.hamcrest.Matchers.equalTo
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class DummyWebhookEndpointTest extends AbstractWebMvcTest {

    def "it should accept payment result notification from dummy pay"() {
        given:
        String raw = """
                    {
                        "dummyPaymentId": "${UUID.randomUUID().toString()}",
                        "onlinePaymentId": "2763d14ba43747b894911acafdc70358",
                        "amount": 3.01,
                        "result": "SUCCESS"
                    }
                """

        and:
        def command = anOnlinePaymentNotification()
                .sendTo(anOnlinePayment().idIs("2763d14ba43747b894911acafdc70358").build())
                .amountIs(3.01)
                .succeed()
                .text(raw)
                .build()
        dummyNotifyPaymentResultCommandAssembler.from(raw) >> command


        when:
        def resultActions = mockMvc.perform(post("/webhook/dummypay/payment")
                .content(raw))

        then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("result", equalTo("OK")))
        and:
        this.onlinePaymentNotificationSubscriber.handle(command)
    }
}
