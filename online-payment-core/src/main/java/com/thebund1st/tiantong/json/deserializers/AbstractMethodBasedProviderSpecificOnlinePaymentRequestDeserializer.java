package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.core.MethodMatcher;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer
        extends JsonDeserializer<ProviderSpecificOnlinePaymentRequest>
        implements MethodMatcher {

    @Getter
    @Setter
    private ObjectMapper objectMapper;

    public abstract ProviderSpecificOnlinePaymentRequest deserialize(String json);
}
