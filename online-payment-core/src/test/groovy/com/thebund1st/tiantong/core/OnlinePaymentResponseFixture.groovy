package com.thebund1st.tiantong.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.thebund1st.tiantong.commands.OnlinePaymentFailureNotification
import com.thebund1st.tiantong.events.EventIdentifier
import com.thebund1st.tiantong.utils.Randoms

import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static java.time.LocalDateTime.now

class OnlinePaymentResponseFixture {
    private OnlinePaymentResponse target = new OnlinePaymentResponse()

    def idIs(String value) {
        idIs(OnlinePaymentResponse.Identifier.of(value))
    }

    def idIs(OnlinePaymentResponse.Identifier id) {
        target.setId(id)
        this
    }

    def amountIs(double amount) {
        target.setAmount(amount)
        this
    }

    def to(OnlinePayment.Identifier value) {
        target.setOnlinePaymentId(value)
        this
    }

    def succeeded() {
        target.setCode(OnlinePaymentResponse.Code.SUCCESS)
        this
    }

    def at(LocalDateTime now) {
        target.setCreatedAt(now)
        this
    }

    def build() {
        target
    }

    static def anOnlinePaymentResponse() {
        new OnlinePaymentResponseFixture()
                .idIs(OnlinePaymentResponse.Identifier.of(Randoms.randomStr()))
                .amountIs(100.00)
                .to(anOnlinePayment().build().id)
                .succeeded()
                .at(now())
    }
}
