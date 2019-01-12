package com.thebund1st.tiantong.commands;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.events.EventIdentifier;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class OnlinePaymentFailureNotification {
    private EventIdentifier eventId;
    private OnlinePayment.Identifier onlinePaymentId;
    //FIXME introduce monetary amount
    private double amount;
    @Setter
    private String raw;

    public OnlinePaymentFailureNotification(EventIdentifier eventId,
                                            OnlinePayment.Identifier onlinePaymentId, double amount) {
        this.eventId = eventId;
        this.onlinePaymentId = onlinePaymentId;
        this.amount = amount;
    }
}
