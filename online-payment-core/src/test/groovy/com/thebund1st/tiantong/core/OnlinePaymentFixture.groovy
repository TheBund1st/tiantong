package com.thebund1st.tiantong.core

import java.time.ZonedDateTime

import static java.time.ZonedDateTime.now

class OnlinePaymentFixture {
    private OnlinePayment target

    OnlinePaymentFixture() {
        this.target = new OnlinePayment(OnlinePayment.Identifier.of(UUID.randomUUID().toString()), now())
    }

    def amountIs(double amount) {
        target.setAmount(amount)
        this
    }

    def build() {
        target
    }

    static def anOnlinePayment() {
        new OnlinePaymentFixture()
                .amountIs(100.00)
    }

}
