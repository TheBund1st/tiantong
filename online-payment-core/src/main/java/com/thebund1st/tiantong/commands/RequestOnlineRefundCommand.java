package com.thebund1st.tiantong.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RequestOnlineRefundCommand {
    private String onlinePaymentId;
}
