package com.thebund1st.tiantong.events;

import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class OnlinePaymentSuccessEvent {
    private OnlinePayment.Identifier onlinePaymentId;
    private OnlinePayment.Correlation correlation;
    private LocalDateTime when;
}
