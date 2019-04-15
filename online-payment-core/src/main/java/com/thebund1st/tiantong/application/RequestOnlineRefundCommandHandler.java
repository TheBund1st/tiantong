package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.RequestOnlineRefundCommand;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class RequestOnlineRefundCommandHandler {

    public OnlineRefund handle(RequestOnlineRefundCommand command) {
        return null;
    }
}
