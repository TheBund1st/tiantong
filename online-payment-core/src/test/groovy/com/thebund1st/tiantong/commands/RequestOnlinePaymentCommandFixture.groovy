package com.thebund1st.tiantong.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.utils.Randoms

class RequestOnlinePaymentCommandFixture {
    private RequestOnlinePaymentCommand target = new RequestOnlinePaymentCommand()
    private Map providerSpecificInfo = [:]

    def amountIs(double amount) {
        target.setAmount(amount)
        this
    }

    def by(OnlinePayment.Method method) {
        target.setMethod(method.value)
        this
    }

    def byWeChatPayJsApi() {
        by(OnlinePayment.Method.of("WECHAT_PAY_JSAPI"))
    }

    def byWeChatPayNative() {
        by(OnlinePayment.Method.of("WECHAT_PAY_NATIVE"))
    }

    def byDummy() {
        by(OnlinePayment.Method.of("DUMMY"))
    }

    def correlatedWith(OnlinePayment.Correlation correlation) {
        target.setCorrelation(correlation)
        this
    }

    def withSubject(String value) {
        target.setSubject(value)
        this
    }

    def withBody(String value) {
        target.setBody(value)
        this
    }

    def withOpenId(String openId) {
        this.providerSpecificInfo['openId'] = openId
        this
    }

    def withDummySpecificInfo() {
        this.providerSpecificInfo['dummy'] = "dummy"
        this
    }

    def build() {
        target.setProviderSpecificInfo(new ObjectMapper().writeValueAsString(providerSpecificInfo))
        target
    }

    static def aRequestOnlinePaymentCommand() {
        new RequestOnlinePaymentCommandFixture()
                .amountIs(100.00)
                .byWeChatPayNative()
                .correlatedWith(OnlinePayment.Correlation.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
                .withSubject("Subject")
                .withBody("Body")
    }
}
