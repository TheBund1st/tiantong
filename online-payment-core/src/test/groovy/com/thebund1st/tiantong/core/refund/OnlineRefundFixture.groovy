package com.thebund1st.tiantong.core.refund

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentFixture
import com.thebund1st.tiantong.utils.Randoms

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlineRefundFixture {
    private OnlineRefund target = new OnlineRefund()

    def idIs(OnlineRefund.Identifier identifier) {
        target.setId(identifier)
        this
    }

    def with(OnlinePaymentFixture fixture) {
        with(fixture.build())
    }

    def with(OnlinePayment op) {
        this.target.setOnlinePaymentId(op.getId())
        this.target.setAmount(op.amount)
        this.target.setOnlinePaymentAmount(op.amount)
        this
    }

    def build() {
        target
    }

    static def anOnlineRefund() {
        new OnlineRefundFixture()
                .idIs(OnlineRefund.Identifier.of(Randoms.randomStr()))
                .with(anOnlinePayment())
    }
}
