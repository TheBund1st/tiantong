package com.thebund1st.tiantong.web.webhooks;

import com.thebund1st.tiantong.commands.NotifyOnlinePaymentResultCommand;

public interface NotifyOnlinePaymentResultCommandAssembler {

    NotifyOnlinePaymentResultCommand from(String rawNotification);
}
