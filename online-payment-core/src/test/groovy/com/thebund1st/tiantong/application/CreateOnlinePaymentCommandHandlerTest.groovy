package com.thebund1st.tiantong.application


import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator
import com.thebund1st.tiantong.core.OnlinePaymentRepository
import com.thebund1st.tiantong.time.Clock
import spock.lang.Specification

import java.time.Duration
import java.time.LocalDateTime

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand
import static com.thebund1st.tiantong.core.OnlinePayment.Status.PENDING

class CreateOnlinePaymentCommandHandlerTest extends Specification {

    private OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator = Mock()
    private OnlinePaymentRepository onlinePaymentRepository = Mock()
    private Clock clock = Mock()
    private CreateOnlinePaymentCommandHandler target

    def setup() {
        target = new CreateOnlinePaymentCommandHandler(
                onlinePaymentIdentifierGenerator, onlinePaymentRepository, clock)
        target.setExpires(Duration.ofMinutes(30))
    }

    def "it should create an online payment"() {
        given:
        def command = aRequestOnlinePaymentCommand().build()
        def now = LocalDateTime.now()
        def onlinePaymentId = OnlinePayment.Identifier.of("1")

        and:
        clock.now() >> now
        onlinePaymentIdentifierGenerator.nextIdentifier() >> onlinePaymentId

        when:
        def actual = target.handle(command)

        then:
        1 * onlinePaymentRepository.save(_ as OnlinePayment)

        and:
        assert actual.id == onlinePaymentId
        assert actual.amount == command.amount
        assert actual.createdAt == now
        assert actual.lastModifiedAt == now
        assert actual.status == PENDING
        assert actual.method == OnlinePayment.Method.of(command.method)
        assert actual.correlation == command.correlation
        assert actual.subject == command.subject
        assert actual.body == command.body
        assert actual.providerSpecificInfo == command.providerSpecificInfo
        assert actual.expiresAt == now.plusMinutes(30)
    }

}