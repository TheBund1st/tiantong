package com.thebund1st.tiantong.core.exceptions;

import com.thebund1st.tiantong.core.OnlinePayment;

public class NoSuchOnlinePaymentProviderGatewayException extends RuntimeException {

    public NoSuchOnlinePaymentProviderGatewayException(OnlinePayment onlinePayment) {
        super(String.format("Cannot find ProviderSpecificCreateOnlinePaymentGateway for OnlinePayment[%s] with method[%s]"
                , onlinePayment.getId(), onlinePayment.getMethod().getValue()));
    }
}
