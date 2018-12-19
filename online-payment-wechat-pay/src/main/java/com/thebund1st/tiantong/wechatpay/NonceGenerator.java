package com.thebund1st.tiantong.wechatpay;

import java.util.UUID;

public class NonceGenerator {
    public String next() {
        return UUID.randomUUID().toString();
    }
}
