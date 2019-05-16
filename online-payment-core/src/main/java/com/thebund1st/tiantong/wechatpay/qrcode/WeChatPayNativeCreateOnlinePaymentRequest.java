package com.thebund1st.tiantong.wechatpay.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WeChatPayNativeCreateOnlinePaymentRequest
        implements ProviderSpecificCreateOnlinePaymentRequest {
    @NotBlank
    @JsonProperty("product_id")
    private String productId;
}
