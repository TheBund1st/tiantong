package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;

public class FakeOnlinePaymentNotificationException extends RuntimeException {

    public FakeOnlinePaymentNotificationException(OnlinePayment onlinePayment,
                                                  OnlinePaymentResultNotification notification) {
        super(String.format("Online Payment [%s][%s] failed to handle mismatch amount [%s]",
                onlinePayment.getId().getValue(), onlinePayment.getAmount(), notification));
    }

    public FakeOnlinePaymentNotificationException(String rawNotification) {
        super(String.format("Failed to parse %s", rawNotification));
    }
}
