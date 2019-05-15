package com.thebund1st.tiantong.web.rest

import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand
import com.thebund1st.tiantong.web.AbstractWebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.not
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

//TODO any simpler way to test the request mapping binding?
@TestPropertySource(properties = "tiantong.http.request.mapping.prefix=/foo")
class OnlinePaymentRestControllerRequestMappingTest extends AbstractWebMvcTest {

    def "it should bind make online payment request with configured prefix"() {
        when:
        def resultActions = mockMvc.perform(post("/foo/online/payments")
                .contentType(APPLICATION_JSON_UTF8)
        )

        then:
        resultActions
                .andExpect(status().is(not(equalTo(HttpStatus.NOT_FOUND.value()))))
    }

    def "it should bind sync online payment result request with configured prefix"() {
        given:
        syncOnlinePaymentResultCommandHandler.handle(_ as SyncOnlinePaymentResultCommand) >> Optional.empty()

        when:
        //@formatter:off
        def then = given()
            .post("/foo/online/payments/anything/resultSynchronizations")
        .then()
        //@formatter:on

        then:
        then.statusCode(not(equalTo(HttpStatus.NOT_FOUND.value())))
    }

}
