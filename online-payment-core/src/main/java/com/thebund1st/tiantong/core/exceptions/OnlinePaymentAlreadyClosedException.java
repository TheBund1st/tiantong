package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.events.OnlinePaymentFailureNotificationReceivedEvent;
import com.thebund1st.tiantong.events.OnlinePaymentSuccessNotificationReceivedEvent;

public class OnlinePaymentAlreadyClosedException extends RuntimeException {

    public OnlinePaymentAlreadyClosedException(OnlinePayment.Identifier id, OnlinePayment.Status status,
                                               OnlinePaymentSuccessNotificationReceivedEvent event) {
        super(String.format("Online Payment [%s] failed to handle [%s] as it has been marked as %s",
                id.getValue(), event, status));
    }

    public OnlinePaymentAlreadyClosedException(OnlinePayment.Identifier id, OnlinePayment.Status status,
                                               OnlinePaymentFailureNotificationReceivedEvent event) {
        super(String.format("Online Payment [%s] failed to handle [%s] as it has been marked as %s",
                id.getValue(), event, status));
    }
}
