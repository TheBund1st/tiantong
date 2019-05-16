package com.thebund1st.tiantong.wechatpay.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;
import lombok.Data;

@Data
public class WeChatPayNativeLaunchOnlinePaymentRequest
        implements ProviderSpecificLaunchOnlinePaymentRequest {
    @JsonProperty("code_url")
    private String codeUrl;

}
