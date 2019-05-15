package com.thebund1st.tiantong.core


import com.thebund1st.tiantong.utils.Randoms

import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static java.time.LocalDateTime.now

class OnlinePaymentResponseFixture {
    private OnlinePaymentResultNotification target = new OnlinePaymentResultNotification()

    def idIs(String value) {
        idIs(OnlinePaymentResultNotification.Identifier.of(value))
    }

    def idIs(OnlinePaymentResultNotification.Identifier id) {
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
        target.setCode(OnlinePaymentResultNotification.Code.SUCCESS)
        this
    }

    def closed() {
        target.setCode(OnlinePaymentResultNotification.Code.CLOSED)
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
                .idIs(OnlinePaymentResultNotification.Identifier.of(Randoms.randomStr()))
                .amountIs(100.00)
                .to(anOnlinePayment().build().id)
                .succeeded()
                .at(now())
    }

}
