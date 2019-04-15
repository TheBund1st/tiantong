package com.thebund1st.tiantong.core;

import com.thebund1st.tiantong.core.refund.OnlineRefund;

public interface OnlineRefundProviderGateway {
    void request(OnlineRefund onlineRefund);
}
