package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;

public class OnlinePaymentAlreadyClosedException extends RuntimeException {

    public OnlinePaymentAlreadyClosedException(OnlinePayment onlinePayment,
                                               OnlinePaymentResultNotification notification) {
        super(String.format("Online Payment [%s] has been marked as %s, it failed to handle [%s]",
                onlinePayment.getId().getValue(), onlinePayment.getStatus(), notification));
    }

    public OnlinePaymentAlreadyClosedException(OnlinePayment onlinePayment) {
        super(String.format("Online Payment [%s] has been marked as %s, it failed to close",
                onlinePayment.getId().getValue(), onlinePayment.getStatus()));
    }
}
