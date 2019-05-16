package com.thebund1st.tiantong.core.payment;

import lombok.Data;

/**
 * A flattened {@link ProviderSpecificCreateOnlinePaymentRequest}.
 * It can be used as default value.
 */
@Data
public class FlattenedProviderSpecificCreateOnlinePaymentRequest
        implements ProviderSpecificCreateOnlinePaymentRequest {
    private String text;
}
