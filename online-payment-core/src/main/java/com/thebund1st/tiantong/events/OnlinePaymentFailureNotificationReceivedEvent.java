package com.thebund1st.tiantong.events;

import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OnlinePaymentFailureNotificationReceivedEvent {
    private OnlinePayment.Identifier onlinePaymentId;
}
