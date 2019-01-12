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

    def build() {
        target.setProviderSpecificInfo(new ObjectMapper().writeValueAsString(providerSpecificInfo))
        target
    }

    static def aRequestOnlinePaymentCommand() {
        new RequestOnlinePaymentCommandFixture()
                .amountIs(100.00)
                .by(OnlinePayment.Method.of("WECHAT_PAY_NATIVE"))
                .correlatedWith(OnlinePayment.Correlation.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
                .withSubject("Subject")
                .withBody("Body")
                .withOpenId(Randoms.randomStr())
    }

}
