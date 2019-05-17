package com.thebund1st.tiantong.wechatpay.webhooks;

import com.thebund1st.tiantong.web.webhooks.NotifyOnlinePaymentResultResponseBodyAssembler;

public class WeChatPayNotifyOnlinePaymentResultResponseBodyAssembler implements NotifyOnlinePaymentResultResponseBodyAssembler {

    private static final String RESPONSE_BODY = "<xml>\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";


    @Override
    public String toResponseBody() {
        return RESPONSE_BODY;
    }
}
