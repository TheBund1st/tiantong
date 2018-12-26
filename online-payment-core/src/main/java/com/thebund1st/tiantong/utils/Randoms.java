package com.thebund1st.tiantong.utils;

import java.util.UUID;

public final class Randoms {
    public static String randomStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
