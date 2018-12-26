package com.thebund1st.tiantong.core

import com.thebund1st.tiantong.utils.Randoms

import static java.time.LocalDateTime.now

class OnlinePaymentFixture {
    private OnlinePayment target

    OnlinePaymentFixture() {
        this.target = new OnlinePayment(OnlinePayment.Identifier.of(Randoms.randomStr()), now())
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

    def build() {
        target
    }

    static def anOnlinePayment() {
        new OnlinePaymentFixture()
                .amountIs(100.00)
                .by(OnlinePayment.Method.of("WECHAT_PAY_NATIVE"))
                .correlatedWith(OnlinePayment.Correlation.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
    }

}
