package com.thebund1st.tiantong.application.scheduling;

import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.Data;

@Data
public class OnlinePaymentResultSynchronizationJob {

    private OnlinePayment.Identifier onlinePaymentId;

}
