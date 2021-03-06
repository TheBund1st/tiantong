package com.thebund1st.tiantong.commands;

import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyOnlinePaymentResultCommand {
    private OnlinePayment.Identifier onlinePaymentId;
    //FIXME introduce monetary amount
    private double amount;
    private String text;
    private boolean success;

    public NotifyOnlinePaymentResultCommand(OnlinePayment.Identifier onlinePaymentId,
                                            double amount, boolean success, String text) {
        this.onlinePaymentId = onlinePaymentId;
        this.amount = amount;
        this.text = text;
        this.success = success;
    }
}
