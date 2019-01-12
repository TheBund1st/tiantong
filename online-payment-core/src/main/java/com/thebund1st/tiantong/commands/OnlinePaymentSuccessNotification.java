package com.thebund1st.tiantong.commands;

import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OnlinePaymentSuccessNotification {
    private OnlinePayment.Identifier onlinePaymentId;
    //FIXME introduce monetary amount
    private double amount;
    private String text;

    public OnlinePaymentSuccessNotification(OnlinePayment.Identifier onlinePaymentId, double amount, String text) {
        this.onlinePaymentId = onlinePaymentId;
        this.amount = amount;
        this.text = text;
    }
}
