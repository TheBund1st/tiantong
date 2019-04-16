package com.thebund1st.tiantong.commands;

import com.thebund1st.tiantong.core.refund.OnlineRefund;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotifyRefundResultCommand {
    private OnlineRefund.Identifier onlineRefundId;
    private String text;

    public NotifyRefundResultCommand(OnlineRefund.Identifier onlineRefundId,
                                     String text) {
        this.onlineRefundId = onlineRefundId;
        this.text = text;
    }
}
