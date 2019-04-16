package com.thebund1st.tiantong.events;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class OnlineRefundSucceededEvent {
    private OnlineRefund.Identifier onlineRefundId;
    private int onlineRefundVersion;
    private double refundAmount;
    private OnlinePayment.Identifier onlinePaymentId;
    private double paymentAmount;
    private OnlinePayment.Correlation correlation;
    private LocalDateTime when;
}
