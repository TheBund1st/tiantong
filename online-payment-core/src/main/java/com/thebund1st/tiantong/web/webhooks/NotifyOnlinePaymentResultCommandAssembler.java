package com.thebund1st.tiantong.web.webhooks;

import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;

public interface NotifyOnlinePaymentResultCommandAssembler {

    NotifyPaymentResultCommand from(String rawNotification);
}
