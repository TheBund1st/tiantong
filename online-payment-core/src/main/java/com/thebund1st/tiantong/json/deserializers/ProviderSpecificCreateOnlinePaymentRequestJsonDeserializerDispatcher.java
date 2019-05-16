package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.thebund1st.tiantong.commands.CreateOnlinePaymentCommand;
import com.thebund1st.tiantong.core.payment.FlattenedProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.core.MethodMatcherFunction;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher
        extends JsonDeserializer<ProviderSpecificCreateOnlinePaymentRequest>
        implements MethodMatcherFunction<MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer,
        ProviderSpecificCreateOnlinePaymentRequest> {

    private final List<MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer> delegateGroup;

    @Override
    public ProviderSpecificCreateOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        CreateOnlinePaymentCommand command = (CreateOnlinePaymentCommand) jp.getParsingContext()
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

    public ProviderSpecificCreateOnlinePaymentRequest deserialize(String method, String json) {
        return deDeserialize(method, d -> d.deserialize(json));
    }

    private ProviderSpecificCreateOnlinePaymentRequest deDeserialize(String method,
                                                                     Function<MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer, ProviderSpecificCreateOnlinePaymentRequest> function) {
        return dispatchOrElse(delegateGroup,
                () -> OnlinePayment.Method.of(method))
                .apply(function, FlattenedProviderSpecificCreateOnlinePaymentRequest::new);
    }
}
