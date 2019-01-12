package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.commands.OnlinePaymentFailureNotification;
import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification;

public class FakeOnlinePaymentNotificationException extends RuntimeException {

    public FakeOnlinePaymentNotificationException(OnlinePayment.Identifier id, double amount,
                                                  OnlinePaymentSuccessNotification event) {
        super(String.format("Online Payment [%s][%s] failed to handle [%s] due to mismatch amount",
                id.getValue(), amount, event));
    }

    public FakeOnlinePaymentNotificationException(OnlinePayment.Identifier id, double amount,
                                                  OnlinePaymentFailureNotification event) {
        super(String.format("Online Payment [%s][%s] failed to handle [%s] due to mismatch amount",
                id.getValue(), amount, event));
    }
}
