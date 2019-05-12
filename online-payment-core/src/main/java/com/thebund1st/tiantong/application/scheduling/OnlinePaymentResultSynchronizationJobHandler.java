package com.thebund1st.tiantong.application.scheduling;

public interface OnlinePaymentResultSynchronizationJobHandler {
    void handle(OnlinePaymentResultSynchronizationJob command);
}
