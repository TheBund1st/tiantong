package com.thebund1st.tiantong.events


import com.thebund1st.tiantong.core.OnlinePayment

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlinePaymentNotificationEventFixture {
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

    def build() {
        success ?
                new OnlinePaymentSuccessNotificationReceivedEvent(eventIdentifier, onlinePayment.id, amount) :
                new OnlinePaymentFailureNotificationReceivedEvent()
    }

    static def anOnlinePaymentNotificationEvent() {
        new OnlinePaymentNotificationEventFixture()
                .identifiedBy(UUID.randomUUID().toString())
                .amountIs(100.00)
                .sendTo(anOnlinePayment().amountIs(100.00).build())
                .succeed()
    }

}
