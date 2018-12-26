package com.thebund1st.tiantong.events;

import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class OnlinePaymentFailureNotificationReceivedEvent {
    private EventIdentifier eventId;
    private OnlinePayment.Identifier onlinePaymentId;
    //FIXME introduce monetary amount
    private double amount;
    @Setter
    private String raw;

    public OnlinePaymentFailureNotificationReceivedEvent(EventIdentifier eventId,
                                                         OnlinePayment.Identifier onlinePaymentId, double amount) {
        this.eventId = eventId;
        this.onlinePaymentId = onlinePaymentId;
        this.amount = amount;
    }
}
