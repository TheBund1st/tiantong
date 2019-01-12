package com.thebund1st.tiantong.commands

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.events.EventIdentifier

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlinePaymentNotificationFixture {
    private double amount
    private OnlinePayment onlinePayment
    private boolean success
    String text


    def amountIs(double amount) {
        this.amount = amount
        this
    }

    def sendTo(OnlinePayment op) {
        this.onlinePayment = op
        this
    }

    def succeed() {
        this.success = true
        this
    }

    def failed() {
        this.success = false
        this
    }

    def text(String value) {
        this.text = value
        this
    }

    def build() {
        success ?
                new OnlinePaymentSuccessNotification(onlinePayment.id, amount, text) :
                new OnlinePaymentFailureNotification(EventIdentifier.of(""), onlinePayment.id, amount)
    }

    static def anOnlinePaymentNotification() {
        new OnlinePaymentNotificationFixture()
                .amountIs(100.00)
                .sendTo(anOnlinePayment().amountIs(100.00).build())
                .succeed()
                .text("This is raw text from provider")
    }

}
