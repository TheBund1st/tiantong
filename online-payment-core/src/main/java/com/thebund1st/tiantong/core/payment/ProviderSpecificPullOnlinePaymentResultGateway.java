package com.thebund1st.tiantong.core.payment;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;

import java.util.Optional;

/**
 * Gateway for pull online payment result from provider.
 */
public interface ProviderSpecificPullOnlinePaymentResultGateway {

    /**
     *
     * @param onlinePayment The online payment
     * @return empty given the online payment is not paid
     */
    Optional<OnlinePaymentResultNotification> pull(OnlinePayment onlinePayment);

}
