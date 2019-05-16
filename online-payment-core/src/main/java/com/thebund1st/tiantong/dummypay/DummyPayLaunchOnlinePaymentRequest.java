package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;
import lombok.Data;

@Data
public class DummyPayLaunchOnlinePaymentRequest
        implements ProviderSpecificLaunchOnlinePaymentRequest {
    private String dummyId;
}
