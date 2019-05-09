package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import com.thebund1st.tiantong.wechatpay.WeChatPayJsApiSpecificOnlinePaymentRequest;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class WeChatPayJsApiSpecificOnlinePaymentRequestDeserializer
        extends AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer {

    private List<String> methods = Collections.singletonList("WECHAT_PAY_JSAPI");

    @Override
    public ProviderSpecificOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper codec = (ObjectMapper) jp.getCodec();
        return codec.readValue(jp, WeChatPayJsApiSpecificOnlinePaymentRequest.class);
    }

    @Override
    public boolean supports(String method) {
        return methods.contains(method);
    }

    @SneakyThrows
    @Override
    public ProviderSpecificOnlinePaymentRequest deserialize(String json) {
        return getObjectMapper().readValue(json, WeChatPayJsApiSpecificOnlinePaymentRequest.class);
    }
}
