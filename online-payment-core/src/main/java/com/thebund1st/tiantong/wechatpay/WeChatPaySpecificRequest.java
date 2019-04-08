package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
public class WeChatPaySpecificRequest implements ProviderSpecificRequest {

    @Delegate
    private WxPayUnifiedOrderResult delegate;

    public WeChatPaySpecificRequest(WxPayUnifiedOrderResult response) {
        this.delegate = response;
    }
}
