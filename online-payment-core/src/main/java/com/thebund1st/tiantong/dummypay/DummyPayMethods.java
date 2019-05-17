package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.method.Method;

public final class DummyPayMethods {

    private static final Method dummyPay =
            Method.of("DUMMY_PAY");

    public static Method dummyPay() {
        return dummyPay;
    }

}
