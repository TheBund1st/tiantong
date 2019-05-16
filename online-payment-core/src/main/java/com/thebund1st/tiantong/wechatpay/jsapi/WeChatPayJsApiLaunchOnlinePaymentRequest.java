package com.thebund1st.tiantong.wechatpay.jsapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;
import lombok.Data;

@Data
public class WeChatPayJsApiLaunchOnlinePaymentRequest
        implements ProviderSpecificLaunchOnlinePaymentRequest {
    @JsonProperty("appId")
    private String appId;

    @JsonProperty("timeStamp")
    private String timestamp;

    @JsonProperty("nonceStr")
    private String nonceStr;

    @JsonProperty("package")
    private String payload;

    @JsonProperty("signType")
    private String signType;

    @JsonProperty("paySign")
    private String paySign;

}
