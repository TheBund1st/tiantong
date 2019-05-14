package com.thebund1st.tiantong.application


import com.thebund1st.tiantong.core.DomainEventPublisher
import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.events.OnlinePaymentClosedEvent
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class CloseOnlinePaymentCommandHandlerTest extends Specification {

    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private DomainEventPublisher domainEventPublisher = Mock()
    private Clock clock = Mock()
    private CloseOnlinePaymentCommandHandler subject =
            new CloseOnlinePaymentCommandHandler(onlinePaymentRepository,
                    domainEventPublisher,
                    clock)

    def "it should emit online payment is about to close event given the online payment exceeds max result waiting time"() {
        given:
        def now = LocalDateTime.now()
        def onlinePayment = anOnlinePayment()
                .createdAt(now.minusMinutes(30))
                .expires(Duration.ofMinutes(29)).build()

        and:
        clock.now() >> now

        when:
        subject.closeIfNecessary(onlinePayment)

        then:
        1 * onlinePaymentRepository.update(onlinePayment)

        and:
        1 * domainEventPublisher.publish(new OnlinePaymentClosedEvent(
                onlinePaymentId: onlinePayment.id,
                onlinePaymentVersion: onlinePayment.version,
                correlation: onlinePayment.correlation,
                when: now
        ))
    }

    def "it should do nothing online payment does not exceed max result waiting time"() {
        given:
        def now = LocalDateTime.now()
        def onlinePayment = anOnlinePayment()
                .createdAt(now.minusMinutes(30))
                .expires(Duration.ofMinutes(31)).build()

        and:
        clock.now() >> now

        when:
        subject.closeIfNecessary(onlinePayment)

        then:
        0 * onlinePaymentRepository.update(onlinePayment)

        and:
        0 * domainEventPublisher.publish(_ as OnlinePaymentClosedEvent)
    }
}
