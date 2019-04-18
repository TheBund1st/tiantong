package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.thebund1st.tiantong.core.OnlinePayment;

import java.io.IOException;

public class OnlinePaymentIdJsonDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return OnlinePayment.Identifier.of(p.getValueAsString());
    }
}
