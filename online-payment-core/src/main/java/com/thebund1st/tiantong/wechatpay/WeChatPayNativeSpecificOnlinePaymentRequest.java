package com.thebund1st.tiantong.wechatpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WeChatPayNativeSpecificOnlinePaymentRequest implements ProviderSpecificOnlinePaymentRequest {
    @NotBlank
    @JsonProperty("product_id")
    private String productId;
}
