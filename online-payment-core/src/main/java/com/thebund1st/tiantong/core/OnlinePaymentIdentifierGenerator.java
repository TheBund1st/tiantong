package com.thebund1st.tiantong.core;

public interface OnlinePaymentIdentifierGenerator {

    OnlinePayment.Identifier nextIdentifier();
    
}
