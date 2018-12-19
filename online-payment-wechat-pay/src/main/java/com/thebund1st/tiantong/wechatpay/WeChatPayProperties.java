package com.thebund1st.tiantong.wechatpay;

import lombok.Data;

@Data
public class WeChatPayProperties {
    private String appId;

    private String merchantId;

    private String merchantKey;

    private boolean sandbox;
}
