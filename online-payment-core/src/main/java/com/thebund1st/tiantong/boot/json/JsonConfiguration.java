package com.thebund1st.tiantong.boot.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thebund1st.tiantong.json.deserializers.DummyPaySpecificOnlinePaymentRequestDeserializer;
import com.thebund1st.tiantong.json.deserializers.MethodBasedProviderSpecificOnlinePaymentRequestDeserializer;
import com.thebund1st.tiantong.json.deserializers.WeChatPayJsApiSpecificOnlinePaymentRequestDeserializer;
import com.thebund1st.tiantong.json.deserializers.WeChatPayNativeSpecificOnlinePaymentRequestDeserializer;
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
    public DummyPaySpecificOnlinePaymentRequestDeserializer dummyPaySpecificOnlinePaymentRequestDeserializer() {
        DummyPaySpecificOnlinePaymentRequestDeserializer deserializer
                = new DummyPaySpecificOnlinePaymentRequestDeserializer();
        deserializer.setObjectMapper(objectMapper);
        return deserializer;
    }

    @Bean
    public WeChatPayNativeSpecificOnlinePaymentRequestDeserializer weChatPayNativeSpecificOnlinePaymentRequestDeserializer() {
        WeChatPayNativeSpecificOnlinePaymentRequestDeserializer deserializer
                = new WeChatPayNativeSpecificOnlinePaymentRequestDeserializer();
        deserializer.setObjectMapper(objectMapper);
        return deserializer;
    }

    @Bean
    public WeChatPayJsApiSpecificOnlinePaymentRequestDeserializer weChatPayJsApiSpecificOnlinePaymentRequestDeserializer() {
        WeChatPayJsApiSpecificOnlinePaymentRequestDeserializer deserializer
                = new WeChatPayJsApiSpecificOnlinePaymentRequestDeserializer();
        deserializer.setObjectMapper(objectMapper);
        return deserializer;
    }

    @Bean
    public MethodBasedProviderSpecificOnlinePaymentRequestDeserializer methodBasedProviderSpecificOnlinePaymentRequestDeserializer() {
        return new MethodBasedProviderSpecificOnlinePaymentRequestDeserializer(asList(
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
