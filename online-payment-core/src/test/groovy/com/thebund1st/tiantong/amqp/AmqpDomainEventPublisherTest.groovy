package com.thebund1st.tiantong.amqp

import com.thebund1st.tiantong.boot.amqp.AmqpConfiguration
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.events.OnlinePaymentSucceededEventFixture.anOnlinePaymentSucceededEvent
import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await

@Import([TestAmqpConfiguration, AmqpConfiguration, RabbitAutoConfiguration])
@ContextConfiguration
@TestPropertySource(properties = "tiantong.domainEventPublisherDelegate.type=amqp")
class AmqpDomainEventPublisherTest extends Specification {

    @Autowired
    private AmqpDomainEventPublisher subject

    @Autowired
    private RabbitTemplate rabbitTemplate

    def "it should send online payment succeeded event to corresponding exchange"() {
        given:
        def payment = anOnlinePayment().build()

        and:
        def event = anOnlinePaymentSucceededEvent().with(payment).build()

        when:
        subject.publish(event)

        then:
        await().atMost(5, SECONDS).untilAsserted {

            def convert = rabbitTemplate.receive("onlinePaymentSucceededQueue")

            assert convert != null
        }
    }
}
