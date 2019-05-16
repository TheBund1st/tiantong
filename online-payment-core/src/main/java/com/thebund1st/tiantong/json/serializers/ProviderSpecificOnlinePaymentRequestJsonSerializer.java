package com.thebund1st.tiantong.json.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ProviderSpecificOnlinePaymentRequestJsonSerializer {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String serialize(ProviderSpecificCreateOnlinePaymentRequest request) {
        return objectMapper.writeValueAsString(request);
    }
}
