package com.thebund1st.tiantong.web.rest

import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand
import com.thebund1st.tiantong.dummypay.DummyPaySpecificRequest
import com.thebund1st.tiantong.web.AbstractWebMvcTest
import org.springframework.http.HttpStatus

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
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
                .with(command.getProviderSpecificRequest())
                .build()
        //noinspection GroovyAssignabilityCheck
        requestOnlinePaymentCommandHandler.handle(command) >> onlinePayment

        and:
        def dummyProviderSpecificRequest = new DummyPaySpecificRequest(dummyId: "dummyId")
        onlinePaymentProviderGateway.request(onlinePayment, command.providerSpecificRequest) >> dummyProviderSpecificRequest

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
                                "providerSpecificRequest": {
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

    def "it should accept request to sync an online payment"() {
        given:
        def onlinePayment = anOnlinePayment().build()

        and:
        requestOnlinePaymentCommandHandler
                .handle(new SyncOnlinePaymentResultCommand(onlinePayment.id.value)) >> onlinePayment

        when:
        //@formatter:off
        def then = given()
            .post("/api/online/payments/${onlinePayment.id.value}/resultSynchronizations")
        .then()
        //@formatter:on

        then:
        then.statusCode(HttpStatus.OK.value())
    }


}
