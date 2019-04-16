package com.thebund1st.tiantong.core.refund

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentFixture
import com.thebund1st.tiantong.utils.Randoms

import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static java.time.LocalDateTime.now

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
        this.target.setCorrelation(op.correlation)
        this.target.setMethod(op.method)
        this
    }

    def createdAt(LocalDateTime var) {
        target.setCreatedAt(var)
        this
    }

    def lastModifiedAt(LocalDateTime var) {
        target.setLastModifiedAt(var)
        this
    }

    def build() {
        target
    }

    static def anOnlineRefund() {
        new OnlineRefundFixture()
                .idIs(OnlineRefund.Identifier.of(Randoms.randomStr()))
                .with(anOnlinePayment())
                .createdAt(now())
                .lastModifiedAt(now())
    }
}
