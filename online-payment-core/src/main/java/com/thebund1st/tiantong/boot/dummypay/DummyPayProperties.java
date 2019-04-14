package com.thebund1st.tiantong.boot.dummypay;

import lombok.Data;

@Data
public class DummyPayProperties {

    private String webhookEndpointBaseUri;
    private String paymentResultNotificationWebhookEndpointPath = "/webhook/dummypay/payment";

    public String paymentResultNotificationWebhookEndpointUri() {
        return String.format("%s%s", webhookEndpointBaseUri, paymentResultNotificationWebhookEndpointPath);
    }
}
