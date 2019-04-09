package com.thebund1st.tiantong.web.webhooks;

import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;

public interface NotifyPaymentResultCommandAssembler {

    NotifyPaymentResultCommand from(String rawNotification);
}
