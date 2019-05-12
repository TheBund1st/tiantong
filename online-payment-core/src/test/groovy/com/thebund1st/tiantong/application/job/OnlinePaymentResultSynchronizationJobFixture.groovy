package com.thebund1st.tiantong.application.job

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJob

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlinePaymentResultSynchronizationJobFixture {
    private OnlinePaymentResultSynchronizationJob target = new OnlinePaymentResultSynchronizationJob()

    def with(OnlinePayment op) {
        this.target.setOnlinePaymentId(op.getId())
        this
    }

    def build() {
        target
    }

    static def anOnlinePaymentResultSynchronizationJob() {
        new OnlinePaymentResultSynchronizationJobFixture()
                .with(anOnlinePayment().build())
    }
}
