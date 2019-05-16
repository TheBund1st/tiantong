package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.OnlinePayment;

public final class DummyPayMethods {

    private static final OnlinePayment.Method dummyPay =
            OnlinePayment.Method.of("DUMMY_PAY");

    public static OnlinePayment.Method dummyPay() {
        return dummyPay;
    }

}
