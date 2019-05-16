package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.core.MethodMatcher;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import lombok.Getter;
import lombok.Setter;

public abstract class MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer
        extends JsonDeserializer<ProviderSpecificCreateOnlinePaymentRequest>
        implements MethodMatcher {

    @Getter
    @Setter
    private ObjectMapper objectMapper;

    public abstract ProviderSpecificCreateOnlinePaymentRequest deserialize(String json);
}
