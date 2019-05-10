package com.thebund1st.tiantong.web.rest

import com.thebund1st.tiantong.web.AbstractWebMvcTest
import org.springframework.test.context.TestPropertySource

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestPropertySource(properties = "tiantong.refund.enabled=false")
class OnlineRefundRestControllerFeatureToggleTest extends AbstractWebMvcTest {

    def "it should accept refund request"() {
        when:
        def resultActions = mockMvc.perform(post("/api/online/payments/a-unique-string/refunds")
                .contentType(APPLICATION_JSON_UTF8)
        )

        then:
        resultActions
                .andExpect(status().isNotFound())

    }
}
