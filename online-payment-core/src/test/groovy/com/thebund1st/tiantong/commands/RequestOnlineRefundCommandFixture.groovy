package com.thebund1st.tiantong.commands

import com.thebund1st.tiantong.core.refund.OnlineRefund
import com.thebund1st.tiantong.core.refund.OnlineRefundFixture

import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund

class RequestOnlineRefundCommandFixture {
    private RequestOnlineRefundCommand target = new RequestOnlineRefundCommand()
    private OnlineRefund or

    def with(OnlineRefundFixture fixture) {
        with(fixture.build())
    }

    def with(OnlineRefund or) {
        this.or = or
        this
    }

    def build() {
        target.setOnlinePaymentId(or.getOnlinePaymentId().value)
        target
    }

    static def aRequestOnlineRefundCommand() {
        new RequestOnlineRefundCommandFixture()
                .with(anOnlineRefund())
    }
}
