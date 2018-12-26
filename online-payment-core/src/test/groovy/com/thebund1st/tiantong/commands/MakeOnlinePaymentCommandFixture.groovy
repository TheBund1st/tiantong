package com.thebund1st.tiantong.commands

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.utils.Randoms

class MakeOnlinePaymentCommandFixture {
    private MakeOnlinePaymentCommand target = new MakeOnlinePaymentCommand()

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

    def build() {
        target
    }

    static def aMakeOnlinePaymentCommand() {
        new MakeOnlinePaymentCommandFixture()
                .amountIs(100.00)
                .by(OnlinePayment.Method.of("WECHAT_PAY_NATIVE"))
                .correlatedWith(OnlinePayment.Correlation.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
    }

}
