package com.thebund1st.tiantong.boot.core;

import lombok.Data;

import java.time.Duration;

@Data
public class OnlinePaymentResultSynchronizationProperties {

    private Duration delay = Duration.ofMinutes(3);

    private Duration keep = Duration.ofMinutes(30);
}
