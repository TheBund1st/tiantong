package com.thebund1st.tiantong.web.webhooks;

import com.thebund1st.tiantong.commands.NotifyOnlinePaymentResultCommand;

/**
 * Assembler that converts raw webhook request body to a {@link NotifyOnlinePaymentResultCommand}.
 * The signature verification should also be covered here, if any.
 */
public interface NotifyOnlinePaymentResultCommandAssembler {

    NotifyOnlinePaymentResultCommand from(String rawNotification);
}
