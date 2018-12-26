package com.thebund1st.tiantong.core;

public interface OnlinePaymentRepository {

    void save(OnlinePayment model);

    OnlinePayment mustFindBy(OnlinePayment.Identifier id);
}
