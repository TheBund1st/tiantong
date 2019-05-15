package com.thebund1st.tiantong.wechatpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thebund1st.tiantong.core.ProviderSpecificUserAgentOnlinePaymentRequest;
import lombok.Data;

@Data
public class WeChatPayNativeSpecificUserAgentOnlinePaymentRequest
        implements ProviderSpecificUserAgentOnlinePaymentRequest {
    @JsonProperty("code_url")
    private String codeUrl;

}
