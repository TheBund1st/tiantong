package com.thebund1st.tiantong.json

import com.thebund1st.tiantong.events.OnlinePaymentSucceededEvent
import org.springframework.boot.test.json.JacksonTester

import static com.thebund1st.tiantong.events.OnlinePaymentSucceededEventFixture.anOnlinePaymentSucceededEvent
import static org.assertj.core.api.Java6Assertions.assertThat

class OnlinePaymentSucceededEventJsonTest extends AbstractJsonTest {

    private JacksonTester<OnlinePaymentSucceededEvent> json

    def "it should serialize OnlinePaymentSucceededEvent"() {

        given:
        def event = anOnlinePaymentSucceededEvent().build()

        when:
        def content = this.json.write(event)

        then:
        assertThat(content)
                .extractingJsonPathStringValue("@.onlinePaymentId")
                .isEqualTo(event.getOnlinePaymentId().value)
        assertThat(content)
                .extractingJsonPathNumberValue("@.onlinePaymentVersion")
                .isEqualTo(event.getOnlinePaymentVersion())
        assertThat(content)
                .extractingJsonPathNumberValue("@.amount")
                .isEqualTo(event.getAmount())
        assertThat(content)
                .extractingJsonPathStringValue("@.correlation.key")
                .isEqualTo(event.getCorrelation().key)
        assertThat(content)
                .extractingJsonPathStringValue("@.correlation.value")
                .isEqualTo(event.getCorrelation().value)
        //FIXME maybe timestamp is better
//        assertThat(content)
//                .extractingJsonPathStringValue("@.when")
//                .isEqualTo(event.getWhen().toString())
    }
}
