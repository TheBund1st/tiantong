package com.thebund1st.tiantong.wechatpay;

import com.thebund1st.tiantong.core.OnlinePayment;

public final class WeChatPayMethods {

    private static final OnlinePayment.Method jsApi =
            OnlinePayment.Method.of("WECHAT_PAY_JSAPI");

    private static final OnlinePayment.Method _native =
            OnlinePayment.Method.of("WECHAT_PAY_NATIVE");


    public static OnlinePayment.Method weChatPayJsApi() {
        return jsApi;
    }

    public static OnlinePayment.Method weChatPayNative() {
        return _native;
    }

}
