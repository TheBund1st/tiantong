package com.thebund1st.tiantong.web.rest

import com.thebund1st.tiantong.dummypay.DummyPaySpecificRequest
import com.thebund1st.tiantong.web.AbstractWebMvcTest

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand
import static com.thebund1st.tiantong.commands.RequestOnlineRefundCommandFixture.aRequestOnlineRefundCommand
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund
import static org.hamcrest.Matchers.is
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class OnlinePaymentRestControllerTest extends AbstractWebMvcTest {

    def "it should accept make online payment request"() {
        given:
        def command = aRequestOnlinePaymentCommand()
                .byDummy()
                .withDummySpecificInfo().build()


        and:
        def onlinePayment = anOnlinePayment()
                .correlatedWith(command.getCorrelation())
                .amountIs(command.getAmount())
                .byMethod(command.getMethod())
                .build()
        //noinspection GroovyAssignabilityCheck
        requestOnlinePaymentCommandHandler.handle(command) >> onlinePayment

        and:
        def dummyProviderSpecificRequest = new DummyPaySpecificRequest(dummyId: "dummyId")
        onlinePaymentProviderGateway.request(onlinePayment) >> dummyProviderSpecificRequest

        when:
        def resultActions = mockMvc.perform(post("/api/online/payments")
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
                                    "dummy": "dummy"
                                },
                                "subject": "${command.getSubject()}",
                                "body": "${command.getBody()}"
                            }
                        """)
        )

        then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("identifier", is(onlinePayment.getId().value)))
                .andExpect(jsonPath("amount", is(onlinePayment.getAmount())))
                .andExpect(jsonPath("method", is(onlinePayment.getMethod().getValue())))
                .andExpect(jsonPath("providerSpecificRequest.dummyId", is("dummyId")))
    }

    def "it should accept refund request"() {
        given:
        def onlinePayment = anOnlinePayment().idIs("a-unique-string").succeeded().build()
        def command = aRequestOnlineRefundCommand().with(onlinePayment).build()
        def onlineRefund = anOnlineRefund().with(onlinePayment).build()

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
                .andExpect(jsonPath("identifier", is(onlineRefund.getId().value)))
    }
}
