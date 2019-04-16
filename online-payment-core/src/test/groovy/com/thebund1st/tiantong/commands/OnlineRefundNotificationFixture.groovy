package com.thebund1st.tiantong.commands

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.refund.OnlineRefund

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund

class OnlineRefundNotificationFixture {
    private double amount
    private OnlineRefund onlineRefund
    private boolean success
    String text


    def amountIs(double amount) {
        this.amount = amount
        this
    }

    def with(OnlineRefund or) {
        this.onlineRefund = or
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

    def text(String value) {
        this.text = value
        this
    }

    def build() {
        new NotifyRefundResultCommand(onlineRefund.id, text)
    }

    static def anOnlineRefundNotification() {
        new OnlineRefundNotificationFixture()
                .with(anOnlineRefund().build())
                .succeed()
                .text("This is raw text from provider")
    }

}
