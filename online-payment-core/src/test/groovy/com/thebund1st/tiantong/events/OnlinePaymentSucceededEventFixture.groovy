package com.thebund1st.tiantong.events

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentFixture

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlinePaymentSucceededEventFixture {
    private OnlinePaymentSucceededEvent target = new OnlinePaymentSucceededEvent()

    def with(OnlinePaymentFixture fixture) {
        with(fixture.build())
    }

    def with(OnlinePayment op) {
        target.setOnlinePaymentId(op.getId())
        target.setOnlinePaymentVersion(op.getVersion())
        target.setAmount(op.getAmount())
        target.setCorrelation(op.getCorrelation())
        target.setWhen(op.getLastModifiedAt())
        this
    }

    def build() {
        target
    }

    static def anOnlinePaymentSucceededEvent() {
        new OnlinePaymentSucceededEventFixture()
                .with(anOnlinePayment())
    }
}
