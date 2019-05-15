package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.ProviderSpecificUserAgentOnlinePaymentRequest;
import lombok.Data;

@Data
public class DummyPaySpecificRequest implements ProviderSpecificUserAgentOnlinePaymentRequest {
    private String dummyId;
}
