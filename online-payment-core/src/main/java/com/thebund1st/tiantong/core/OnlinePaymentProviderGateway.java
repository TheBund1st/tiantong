package com.thebund1st.tiantong.core;

public interface OnlinePaymentProviderGateway {
    ProviderSpecificRequest request(OnlinePayment onlinePayment);
}
