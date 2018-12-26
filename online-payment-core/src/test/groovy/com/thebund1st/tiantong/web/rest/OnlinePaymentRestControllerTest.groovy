package com.thebund1st.tiantong.web.rest

import com.thebund1st.tiantong.web.AbstractWebMvcTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import static com.thebund1st.tiantong.commands.MakeOnlinePaymentCommandFixture.aMakeOnlinePaymentCommand
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class OnlinePaymentRestControllerTest extends AbstractWebMvcTest {

    def "it should accept make online payment request"() {
        given:
        def command = aMakeOnlinePaymentCommand().build()

        when:
        def resultActions = mockMvc.perform(
                post("/api/online/payments")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content("""
                            {
                                "amount": "${command.getAmount()}",
                                "method": "${command.getMethod()}",
                                "correlation": {
                                    "key":"${command.getCorrelation().getKey()}",
                                    "value": "${command.getCorrelation().getValue()}"
                                }
                            }
                        """)
        )

        then:
        resultActions
                .andExpect(status().isOk())
//                .andExpect(jsonPath('$._links.wechat_pay_native.href', equalTo("")))

        and:
        1 * onlinePaymentCommandHandler.handle(command)

    }
}
