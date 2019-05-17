package com.thebund1st.tiantong.dummypay;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.core.method.Method;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.json.deserializers.MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer;
import lombok.SneakyThrows;

import java.io.IOException;

import static com.thebund1st.tiantong.dummypay.DummyPayMethods.dummyPay;

public class DummyPayCreateOnlinePaymentRequestJsonDeserializer
        extends MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer {

    @Override
    public ProviderSpecificCreateOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper codec = (ObjectMapper) jp.getCodec();
        return codec.readValue(jp, DummyPayCreateOnlinePaymentRequest.class);
    }

    @Override
    public boolean supports(Method method) {
        return dummyPay().equals(method);
    }

    @SneakyThrows
    @Override
    public ProviderSpecificCreateOnlinePaymentRequest deserialize(String json) {
        return getObjectMapper().readValue(json, DummyPayCreateOnlinePaymentRequest.class);
    }
}
