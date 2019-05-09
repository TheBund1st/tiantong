package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import com.thebund1st.tiantong.wechatpay.WeChatPayNativeSpecificOnlinePaymentRequest;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class WeChatPayNativeSpecificOnlinePaymentRequestDeserializer
        extends AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer {

    private List<String> methods = Collections.singletonList("WECHAT_PAY_NATIVE");

    @Override
    public ProviderSpecificOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper codec = (ObjectMapper) jp.getCodec();
        return codec.readValue(jp, WeChatPayNativeSpecificOnlinePaymentRequest.class);
    }

    @Override
    public boolean supports(String method) {
        return methods.contains(method);
    }

    @SneakyThrows
    @Override
    public ProviderSpecificOnlinePaymentRequest deserialize(String json) {
        return getObjectMapper().readValue(json, WeChatPayNativeSpecificOnlinePaymentRequest.class);
    }
}
