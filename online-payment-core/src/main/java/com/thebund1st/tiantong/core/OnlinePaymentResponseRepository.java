package com.thebund1st.tiantong.core;

public interface OnlinePaymentResponseRepository {

    void save(OnlinePaymentResponse model);

    OnlinePaymentResponse mustFindBy(OnlinePaymentResponse.Identifier id);
}
