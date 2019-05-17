package com.thebund1st.tiantong.core.payment;

import com.thebund1st.tiantong.core.OnlinePayment;

/**
 * Gateway for closing online payment to provider.
 */
public interface ProviderSpecificCloseOnlinePaymentGateway {

    /**
     * @param onlinePayment The online payment created
     */
    void close(OnlinePayment onlinePayment);

}
