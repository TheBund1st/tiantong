package com.thebund1st.tiantong.commands

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentFixture

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class RequestOnlineRefundCommandFixture {
    private RequestOnlineRefundCommand target = new RequestOnlineRefundCommand()
    private OnlinePayment op

    def with(OnlinePaymentFixture fixture) {
        with(fixture.build())
    }

    def with(OnlinePayment op) {
        this.op = op
        this
    }

    def build() {
        target.setOnlinePaymentId(op.getId().value)
        target
    }

    static def aRequestOnlineRefundCommand() {
        new RequestOnlineRefundCommandFixture()
                .with(anOnlinePayment())
    }
}
