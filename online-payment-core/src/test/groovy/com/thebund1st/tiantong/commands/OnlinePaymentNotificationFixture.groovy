package com.thebund1st.tiantong.commands


import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.events.EventIdentifier

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlinePaymentNotificationFixture {
    private EventIdentifier eventIdentifier
    private double amount
    private OnlinePayment onlinePayment
    private boolean success

    def identifiedBy(String value) {
        this.eventIdentifier = EventIdentifier.of(value)
        this
    }

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

    def build() {
        success ?
                new OnlinePaymentSuccessNotification(eventIdentifier, onlinePayment.id, amount) :
                new OnlinePaymentFailureNotification(eventIdentifier, onlinePayment.id, amount)
    }

    static def anOnlinePaymentNotification() {
        new OnlinePaymentNotificationFixture()
                .identifiedBy(UUID.randomUUID().toString())
                .amountIs(100.00)
                .sendTo(anOnlinePayment().amountIs(100.00).build())
                .succeed()
    }

}
