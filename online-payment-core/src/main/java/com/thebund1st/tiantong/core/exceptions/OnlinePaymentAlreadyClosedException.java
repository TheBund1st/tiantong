package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;

public class OnlinePaymentAlreadyClosedException extends RuntimeException {

    public OnlinePaymentAlreadyClosedException(OnlinePayment.Identifier id, OnlinePayment.Status status,
                                               NotifyPaymentResultCommand event) {
        super(String.format("Online Payment [%s] failed to handle [%s] as it has been marked as %s",
                id.getValue(), event, status));
    }

}
