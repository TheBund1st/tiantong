package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;

public class FakeOnlinePaymentNotificationException extends RuntimeException {

    public FakeOnlinePaymentNotificationException(OnlinePayment.Identifier id, double amount,
                                                  NotifyPaymentResultCommand event) {
        super(String.format("Online Payment [%s][%s] failed to handle [%s] due to mismatch amount",
                id.getValue(), amount, event));
    }

    public FakeOnlinePaymentNotificationException(String rawNotification) {
        super(String.format("Failed to parse %s", rawNotification));
    }
}
