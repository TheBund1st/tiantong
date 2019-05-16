package com.thebund1st.tiantong.core.payment;

import com.thebund1st.tiantong.core.OnlinePayment;

/**
 * Gateway for creating online payment to provider.
 */
public interface ProviderSpecificCreateOnlinePaymentGateway {

    /**
     * @param onlinePayment The online payment created
     * @param providerSpecificRequest Some providers require specific data to create online payment.
     * @return provider specific
     */
    ProviderSpecificLaunchOnlinePaymentRequest create(OnlinePayment onlinePayment,
                                                      ProviderSpecificCreateOnlinePaymentRequest providerSpecificRequest);
}
