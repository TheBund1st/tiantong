package com.thebund1st.tiantong.wechatpay.jsapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WeChatPayJsApiCreateOnlinePaymentRequest
        implements ProviderSpecificCreateOnlinePaymentRequest {
    @NotBlank
    @JsonProperty("openid")
    private String openId;
}
