package com.thebund1st.tiantong.web.webhooks;

import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification;

public interface NotifyPaymentResultCommandAssembler {

    OnlinePaymentSuccessNotification from(String rawNotification);
}
