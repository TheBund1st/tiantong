package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand;
import com.thebund1st.tiantong.core.EmptyOnlinePaymentRequest;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class MethodBasedProviderSpecificOnlinePaymentRequestDeserializer
        extends JsonDeserializer<ProviderSpecificOnlinePaymentRequest> {

    private final List<AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer> delegateGroup;

    @Override
    public ProviderSpecificOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        RequestOnlinePaymentCommand command = (RequestOnlinePaymentCommand) jp.getParsingContext()
                .getParent().getCurrentValue();
        String method = command.getMethod();
        return deDeserialize(method, d -> {
            try {
                return d.deserialize(jp, ctxt);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ProviderSpecificOnlinePaymentRequest deserialize(String method, String json) {
        return deDeserialize(method, d -> d.deserialize(json));
    }

    private ProviderSpecificOnlinePaymentRequest deDeserialize(String method,
                                                               Function<AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer, ProviderSpecificOnlinePaymentRequest> function) {
        Optional<AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer> deserializer = delegateGroup.stream()
                .filter(d -> d.supports(method))
                .findAny();
        if (deserializer.isPresent()) {
            return function.apply(deserializer.get());
        } else {
            return new EmptyOnlinePaymentRequest();
        }
    }
}
