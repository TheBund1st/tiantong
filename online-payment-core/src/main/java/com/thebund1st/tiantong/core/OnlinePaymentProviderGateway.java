package com.thebund1st.tiantong.core;

public interface OnlinePaymentProviderGateway {
    ProviderSpecificUserAgentOnlinePaymentRequest request(OnlinePayment onlinePayment,
                                                          ProviderSpecificOnlinePaymentRequest providerSpecificRequest);
}
