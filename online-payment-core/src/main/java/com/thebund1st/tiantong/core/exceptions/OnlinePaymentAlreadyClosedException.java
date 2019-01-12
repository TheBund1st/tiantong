package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.commands.OnlinePaymentFailureNotification;
import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification;

public class OnlinePaymentAlreadyClosedException extends RuntimeException {

    public OnlinePaymentAlreadyClosedException(OnlinePayment.Identifier id, OnlinePayment.Status status,
                                               OnlinePaymentSuccessNotification event) {
        super(String.format("Online Payment [%s] failed to handle [%s] as it has been marked as %s",
                id.getValue(), event, status));
    }

    public OnlinePaymentAlreadyClosedException(OnlinePayment.Identifier id, OnlinePayment.Status status,
                                               OnlinePaymentFailureNotification event) {
        super(String.format("Online Payment [%s] failed to handle [%s] as it has been marked as %s",
                id.getValue(), event, status));
    }
}
