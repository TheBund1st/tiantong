package com.thebund1st.tiantong.boot.wechatpay;

import lombok.Data;

@Data
public class WeChatPayProperties {
    private String appId;

    private String merchantId;

    private String merchantKey;

    private boolean sandbox;

    private String webhookEndpointBaseUri;

    private String paymentResultNotificationWebhookEndpointPath = "/webhook/wechatpay/payment";

    private String refundResultNotificationWebhookEndpointPath = "/webhook/wechatpay/refunds";

    public String paymentResultNotificationWebhookEndpointUri() {
        return String.format("%s%s", webhookEndpointBaseUri, paymentResultNotificationWebhookEndpointPath);
    }

    public String refundResultNotificationWebhookEndpointUri() {
        return String.format("%s%s", webhookEndpointBaseUri, refundResultNotificationWebhookEndpointPath);
    }
}
