package com.thebund1st.tiantong.core;

import lombok.Data;

@Data
public class EmptyOnlinePaymentRequest implements ProviderSpecificOnlinePaymentRequest {
    private String shouldBeNull;
}
