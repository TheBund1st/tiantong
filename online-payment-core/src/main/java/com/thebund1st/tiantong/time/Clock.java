package com.thebund1st.tiantong.time;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class Clock {

    //TODO make it configurable
    private ZoneId zoneId = ZoneId.of("Asia/Shanghai");

    public LocalDateTime now() {
        return LocalDateTime.now(zoneId);
    }
}
