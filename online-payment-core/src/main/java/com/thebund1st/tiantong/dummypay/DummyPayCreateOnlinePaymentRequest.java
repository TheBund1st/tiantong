package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import lombok.Data;

@Data
public class DummyPayCreateOnlinePaymentRequest
        implements ProviderSpecificCreateOnlinePaymentRequest {
    private String dummy;
}
