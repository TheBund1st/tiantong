package com.thebund1st.tiantong.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import com.thebund1st.tiantong.dummypay.DummyPaySpecificOnlinePaymentRequest;

import java.io.IOException;

public class ProviderSpecificOnlinePaymentRequestDeserializer
        extends JsonDeserializer<ProviderSpecificOnlinePaymentRequest> {

    @Override
    public ProviderSpecificOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        return mapper.readValue(jp, DummyPaySpecificOnlinePaymentRequest.class);
    }

}
