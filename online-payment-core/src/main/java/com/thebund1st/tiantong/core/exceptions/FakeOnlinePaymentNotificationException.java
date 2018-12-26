package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.events.OnlinePaymentFailureNotificationReceivedEvent;
import com.thebund1st.tiantong.events.OnlinePaymentSuccessNotificationReceivedEvent;

public class FakeOnlinePaymentNotificationException extends RuntimeException {

    public FakeOnlinePaymentNotificationException(OnlinePayment.Identifier id, double amount,
                                                  OnlinePaymentSuccessNotificationReceivedEvent event) {
        super(String.format("Online Payment [%s][%s] failed to handle [%s] due to mismatch amount",
                id.getValue(), amount, event));
    }

    public FakeOnlinePaymentNotificationException(OnlinePayment.Identifier id, double amount,
                                                  OnlinePaymentFailureNotificationReceivedEvent event) {
        super(String.format("Online Payment [%s][%s] failed to handle [%s] due to mismatch amount",
                id.getValue(), amount, event));
    }
}
