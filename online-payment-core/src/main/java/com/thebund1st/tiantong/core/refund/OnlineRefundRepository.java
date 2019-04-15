package com.thebund1st.tiantong.core.refund;

public interface OnlineRefundRepository {
    void save(OnlineRefund or);

    OnlineRefund mustFindBy(OnlineRefund.Identifier id);
}
