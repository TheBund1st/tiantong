package com.thebund1st.tiantong.web.rest


import com.thebund1st.tiantong.web.AbstractWebMvcTest
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

import static com.thebund1st.tiantong.commands.RequestOnlineRefundCommandFixture.aRequestOnlineRefundCommand
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund
import static org.hamcrest.Matchers.is
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class OnlineRefundRestControllerTest extends AbstractWebMvcTest {

    def "it should accept refund request"() {
        given:
        def onlinePayment = anOnlinePayment().idIs("a-unique-string").succeeded().build()
        def onlineRefund = anOnlineRefund().with(onlinePayment).build()
        def command = aRequestOnlineRefundCommand().with(onlineRefund).build()

        and:
        //noinspection GroovyAssignabilityCheck
        requestOnlineRefundCommandHandler.handle(command) >> onlineRefund

        when:
        def resultActions = mockMvc.perform(post("/api/online/payments/a-unique-string/refunds")
                .contentType(APPLICATION_JSON_UTF8)
        )

        then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("identifier", is(onlineRefund.getId().value)))

        and:
        1 * onlineRefundProviderGateway.request(onlineRefund)
    }
}
