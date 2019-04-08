package com.thebund1st.tiantong.wechatpay;

import lombok.SneakyThrows;

import java.net.InetAddress;

public class IpAddressExtractor {
    @SneakyThrows
    public String getLocalhostAddress() {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
