package com.thebund1st.tiantong.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.thebund1st.tiantong.core.OnlinePayment;

import java.io.IOException;

public class OnlinePaymentIdJsonSerializer extends JsonSerializer<OnlinePayment.Identifier> {

    @Override
    public void serialize(OnlinePayment.Identifier value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getValue());
    }
}
