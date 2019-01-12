package com.thebund1st.tiantong.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.thebund1st.tiantong.events.EventIdentifier
import com.thebund1st.tiantong.commands.OnlinePaymentFailureNotification
import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification
import com.thebund1st.tiantong.utils.Randoms

import static java.time.LocalDateTime.now

class OnlinePaymentFixture {
    private OnlinePayment target
    private Map providerSpecificInfo = [:]

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
        def event = new OnlinePaymentSuccessNotification(EventIdentifier.of(Randoms.randomStr()),
                target.getId(), target.getAmount())
        target.on(event, now())
        this
    }

    def failed() {
        def event = new OnlinePaymentFailureNotification(EventIdentifier.of(Randoms.randomStr()),
                target.getId(), target.getAmount())
        target.on(event, now())
        this
    }

    def subjectIs(String value) {
        target.setSubject(value)
        this
    }

    def bodyIs(String value) {
        target.setBody(value)
        this
    }

    def withOpenId(String openId) {
        this.providerSpecificInfo['openId'] = openId
        this
    }

    def withProductId(String value) {
        this.providerSpecificInfo['productId'] = value
        this
    }

    def build() {
        target.setProviderSpecificInfo(new ObjectMapper().writeValueAsString(providerSpecificInfo))
        target
    }

    static def anOnlinePayment() {
        new OnlinePaymentFixture()
                .idIs(OnlinePayment.Identifier.of(Randoms.randomStr()))
                .amountIs(100.00)
                .by(OnlinePayment.Method.of("WECHAT_PAY_JSAPI"))
                .correlatedWith(OnlinePayment.Correlation.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
                .subjectIs("This is a test product")
                .bodyIs("This is a test product details")
                .withOpenId(Randoms.randomStr())
    }
}
