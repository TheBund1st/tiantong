package com.thebund1st.tiantong.wechatpay.jsapi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.json.deserializers.MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer;
import lombok.SneakyThrows;

import java.io.IOException;

import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayJsApi;

public class WeChatPayJsApiCreateOnlinePaymentRequestJsonDeserializer
        extends MethodBasedProviderSpecificCreateOnlinePaymentRequestJsonDeserializer {

    @Override
    public ProviderSpecificCreateOnlinePaymentRequest deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper codec = (ObjectMapper) jp.getCodec();
        return codec.readValue(jp, WeChatPayJsApiCreateOnlinePaymentRequest.class);
    }

    @Override
    public boolean supports(OnlinePayment.Method method) {
        return weChatPayJsApi().equals(method);
    }

    @SneakyThrows
    @Override
    public ProviderSpecificCreateOnlinePaymentRequest deserialize(String json) {
        return getObjectMapper().readValue(json, WeChatPayJsApiCreateOnlinePaymentRequest.class);
    }
}
