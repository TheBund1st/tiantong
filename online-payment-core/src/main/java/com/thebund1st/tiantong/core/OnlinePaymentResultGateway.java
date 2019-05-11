package com.thebund1st.tiantong.core;

import java.util.Optional;

public interface OnlinePaymentResultGateway {

    Optional<OnlinePaymentResultNotification> pull(OnlinePayment onlinePayment);

}
