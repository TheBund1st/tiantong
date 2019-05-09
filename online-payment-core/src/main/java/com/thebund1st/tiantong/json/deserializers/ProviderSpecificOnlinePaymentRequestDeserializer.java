package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@RequiredArgsConstructor
@JsonComponent
public class ProviderSpecificOnlinePaymentRequestDeserializer
        extends JsonDeserializer<ProviderSpecificOnlinePaymentRequest> {

    private final MethodBasedProviderSpecificOnlinePaymentRequestDeserializer dispatcher;

    @Override
    public ProviderSpecificOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return dispatcher.deserialize(jp, ctxt);
    }
}
