package com.thebund1st.tiantong.wechatpay;

import com.thebund1st.tiantong.core.method.Method;

public final class WeChatPayMethods {

    private static final Method jsApi =
            Method.of("WECHAT_PAY_JSAPI");

    private static final Method _native =
            Method.of("WECHAT_PAY_NATIVE");


    public static Method weChatPayJsApi() {
        return jsApi;
    }

    public static Method weChatPayNative() {
        return _native;
    }

}
