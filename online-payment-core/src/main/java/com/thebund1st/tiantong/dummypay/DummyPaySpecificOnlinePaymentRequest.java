package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import lombok.Data;

@Data
public class DummyPaySpecificOnlinePaymentRequest implements ProviderSpecificOnlinePaymentRequest {
    private String dummy;
}
