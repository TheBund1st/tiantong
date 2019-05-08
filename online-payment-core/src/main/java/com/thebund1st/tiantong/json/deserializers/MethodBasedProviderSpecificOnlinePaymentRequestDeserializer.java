package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand;
import com.thebund1st.tiantong.core.EmptyOnlinePaymentRequest;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@JsonComponent
public class MethodBasedProviderSpecificOnlinePaymentRequestDeserializer
        extends JsonDeserializer<ProviderSpecificOnlinePaymentRequest> {

    @Autowired
    private List<AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer> delegateGroup;

    @Override
    public ProviderSpecificOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        RequestOnlinePaymentCommand command = (RequestOnlinePaymentCommand) jp.getParsingContext()
                .getParent().getCurrentValue();
        Optional<AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer> deserializer = delegateGroup.stream()
                .filter(d -> d.supports(command.getMethod()))
                .findAny();
        if (deserializer.isPresent()) {
            return deserializer.get().deserialize(jp, ctxt);
        } else {
            return new EmptyOnlinePaymentRequest();
        }
    }
}
