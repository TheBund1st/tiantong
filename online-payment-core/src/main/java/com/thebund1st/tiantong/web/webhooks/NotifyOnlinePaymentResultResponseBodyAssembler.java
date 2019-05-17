package com.thebund1st.tiantong.web.webhooks;

/**
 * Assembler that assembles response to acknowledge the webhook is received.
 */
public interface NotifyOnlinePaymentResultResponseBodyAssembler {

    String toResponseBody();
}
