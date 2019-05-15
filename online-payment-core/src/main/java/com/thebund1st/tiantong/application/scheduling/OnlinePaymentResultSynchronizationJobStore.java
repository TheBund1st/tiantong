package com.thebund1st.tiantong.application.scheduling;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OnlinePaymentResultSynchronizationJobStore {
    Page<OnlinePaymentResultSynchronizationJob> find(LocalDateTime localDateTime, Pageable pageable);
}
