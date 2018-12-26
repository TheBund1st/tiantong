package com.thebund1st.tiantong.core

import com.thebund1st.tiantong.events.EventIdentifier
import com.thebund1st.tiantong.events.OnlinePaymentFailureNotificationReceivedEvent
import com.thebund1st.tiantong.events.OnlinePaymentSuccessNotificationReceivedEvent
import com.thebund1st.tiantong.utils.Randoms

import static java.time.LocalDateTime.now

class OnlinePaymentFixture {
    private OnlinePayment target

    OnlinePaymentFixture() {
        this.target = new OnlinePayment()
        target.setCreatedAt(now())
        target.setLastModifiedAt(now())
    }

    def idIs(String value) {
        idIs(OnlinePayment.Identifier.of(value))
    }

    def idIs(OnlinePayment.Identifier id) {
        target.setId(id)
        this
    }

    def amountIs(double amount) {
        target.setAmount(amount)
        this
    }

    def by(OnlinePayment.Method method) {
        target.setMethod(method)
        this
    }

    def correlatedWith(OnlinePayment.Correlation correlation) {
        target.setCorrelation(correlation)
        this
    }

    def succeeded() {
        def event = new OnlinePaymentSuccessNotificationReceivedEvent(EventIdentifier.of(Randoms.randomStr()),
                target.getId(), target.getAmount())
        target.on(event, now())
        this
    }

    def failed() {
        def event = new OnlinePaymentFailureNotificationReceivedEvent(EventIdentifier.of(Randoms.randomStr()),
                target.getId(), target.getAmount())
        target.on(event, now())
        this
    }

    def subjectIs(String value) {
        target.setSubject(value)
        this
    }

    def build() {
        target
    }

    static def anOnlinePayment() {
        new OnlinePaymentFixture()
                .idIs(OnlinePayment.Identifier.of(Randoms.randomStr()))
                .amountIs(100.00)
                .by(OnlinePayment.Method.of("WECHAT_PAY_NATIVE"))
                .correlatedWith(OnlinePayment.Correlation.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
                .subjectIs("This is a test product")
    }
}
