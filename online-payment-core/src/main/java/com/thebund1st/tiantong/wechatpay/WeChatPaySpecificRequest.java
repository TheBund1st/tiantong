package com.thebund1st.tiantong.wechatpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import lombok.Data;

@Data
public class WeChatPaySpecificRequest implements ProviderSpecificRequest {

    private WxPayUnifiedOrderResult delegate;

    public WeChatPaySpecificRequest(WxPayUnifiedOrderResult response) {
        this.delegate = response;
    }

    @JsonProperty("code_url")
    public String getCodeUrl() {
        return delegate.getCodeURL();
    }
}
