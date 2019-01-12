package com.thebund1st.tiantong.events;

import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class OnlinePaymentSuccessEvent {
    private OnlinePayment.Identifier onlinePaymentId;
    private int onlinePaymentVersion;
    private OnlinePayment.Correlation correlation;
    private double amount;
    private LocalDateTime when;
    private String notificationBody;
}
