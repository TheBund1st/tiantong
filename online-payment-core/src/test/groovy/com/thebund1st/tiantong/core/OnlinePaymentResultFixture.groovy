package com.thebund1st.tiantong.core


import com.thebund1st.tiantong.utils.Randoms

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlinePaymentResultFixture {
    private OnlinePaymentResultNotification target = new OnlinePaymentResultNotification()


    def idIs(String value) {
        target.id = OnlinePaymentResultNotification.Identifier.of(value)
        this
    }

    def amountIs(double amount) {
        target.amount = amount
        this
    }

    def sendTo(OnlinePayment op) {
        this.target.onlinePaymentId = op.id
        this.target.amount = op.amount
        this
    }

    def succeed() {
        this.target.code = OnlinePaymentResultNotification.Code.SUCCESS
        this
    }

    def failed() {
        this.target.code = OnlinePaymentResultNotification.Code.FAILURE
        this
    }

    def text(String value) {
        this.target.text = value
        this
    }

    def build() {
        target
    }

    static def anOnlinePaymentResult() {
        new OnlinePaymentResultFixture()
                .idIs(Randoms.randomStr())
                .amountIs(100.00)
                .sendTo(anOnlinePayment().amountIs(100.00).build())
                .succeed()
                .text("This is raw text from provider")
    }

}
