package com.thebund1st.tiantong.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.thebund1st.tiantong.utils.Randoms

import static com.thebund1st.tiantong.commands.OnlinePaymentNotificationFixture.anOnlinePaymentNotification
import static com.thebund1st.tiantong.core.OnlinePaymentResponseFixture.anOnlinePaymentResponse
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

    def byMethod(String method) {
        by(OnlinePayment.Method.of(method))
    }

    def correlatedWith(OnlinePayment.Correlation correlation) {
        target.setCorrelation(correlation)
        this
    }

    def succeeded() {
        def event = anOnlinePaymentResponse()
                .to(target.getId())
                .amountIs(target.getAmount())
                .succeeded()
                .build()
        target.on(event)
        this
    }

    def failed() {
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

    def withProviderSpecificInfo(Map providerSpecificInfo) {
        this.providerSpecificInfo = providerSpecificInfo
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
