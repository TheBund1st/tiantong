package com.thebund1st.tiantong.commands;

import lombok.Data;


@Data
public class SyncOnlinePaymentResultCommand {

    private String onlinePaymentId;

    public SyncOnlinePaymentResultCommand(String onlinePaymentId) {
        this.onlinePaymentId = onlinePaymentId;
    }
}
