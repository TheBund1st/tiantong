package com.thebund1st.tiantong.boot.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.dummypay.DummyPayCreateOnlinePaymentRequestJsonDeserializer;
import com.thebund1st.tiantong.json.deserializers.ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher;
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiCreateOnlinePaymentRequestJsonDeserializer;
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeCreateOnlinePaymentRequestJsonDeserializer;
import com.thebund1st.tiantong.json.serializers.ProviderSpecificOnlinePaymentRequestJsonSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Configuration
@RequiredArgsConstructor
public class JsonConfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    public DummyPayCreateOnlinePaymentRequestJsonDeserializer dummyPaySpecificOnlinePaymentRequestDeserializer() {
        DummyPayCreateOnlinePaymentRequestJsonDeserializer deserializer
                = new DummyPayCreateOnlinePaymentRequestJsonDeserializer();
        deserializer.setObjectMapper(objectMapper);
        return deserializer;
    }

    @Bean
    public WeChatPayNativeCreateOnlinePaymentRequestJsonDeserializer weChatPayNativeSpecificOnlinePaymentRequestDeserializer() {
        WeChatPayNativeCreateOnlinePaymentRequestJsonDeserializer deserializer
                = new WeChatPayNativeCreateOnlinePaymentRequestJsonDeserializer();
        deserializer.setObjectMapper(objectMapper);
        return deserializer;
    }

    @Bean
    public WeChatPayJsApiCreateOnlinePaymentRequestJsonDeserializer weChatPayJsApiSpecificOnlinePaymentRequestDeserializer() {
        WeChatPayJsApiCreateOnlinePaymentRequestJsonDeserializer deserializer
                = new WeChatPayJsApiCreateOnlinePaymentRequestJsonDeserializer();
        deserializer.setObjectMapper(objectMapper);
        return deserializer;
    }

    @Bean
    public ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher methodBasedProviderSpecificOnlinePaymentRequestDeserializer() {
        return new ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher(asList(
                weChatPayJsApiSpecificOnlinePaymentRequestDeserializer(),
                weChatPayJsApiSpecificOnlinePaymentRequestDeserializer(),
                dummyPaySpecificOnlinePaymentRequestDeserializer()
        ));
    }

    @Bean
    public ProviderSpecificOnlinePaymentRequestJsonSerializer providerSpecificOnlinePaymentRequestJsonSerializer() {
        return new ProviderSpecificOnlinePaymentRequestJsonSerializer(objectMapper);
    }
}
