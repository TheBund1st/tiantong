package com.thebund1st.tiantong.web.rest

import com.thebund1st.tiantong.web.AbstractWebMvcTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class OnlinePaymentRestControllerTest extends AbstractWebMvcTest {

    def "it should accept make online payment request"() {
        given:
        def command = aRequestOnlinePaymentCommand().withOpenId("abc").build()


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
                                },
                                "providerSpecificInfo": {
                                    "openId": "abc"
                                },
                                "subject": "${command.getSubject()}",
                                "body": "${command.getBody()}"
                            }
                        """)
        )

        then:
        resultActions
                .andExpect(status().isOk())
//                .andExpect(jsonPath('$._links.wechat_pay_native.href', equalTo("")))

        and:
        //noinspection GroovyAssignabilityCheck
        1 * requestOnlinePaymentCommandHandler.handle({
            it.amount == command.amount
            it.method == command.method
            it.correlation == command.correlation
            it.providerSpecificInfo == command.providerSpecificInfo
            it.subject == command.subject
            it.body == command.body
        })

    }
}
