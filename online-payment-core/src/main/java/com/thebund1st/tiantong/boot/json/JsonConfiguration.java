package com.thebund1st.tiantong.boot.json;

import com.thebund1st.tiantong.json.deserializers.DummyPaySpecificOnlinePaymentRequestDeserializer;
import com.thebund1st.tiantong.json.deserializers.WeChatPayNativeSpecificOnlinePaymentRequestDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JsonConfiguration {

    @Bean
    public DummyPaySpecificOnlinePaymentRequestDeserializer dummyPaySpecificOnlinePaymentRequestDeserializer() {
        return new DummyPaySpecificOnlinePaymentRequestDeserializer();
    }

    @Bean
    public WeChatPayNativeSpecificOnlinePaymentRequestDeserializer weChatPayNativeSpecificOnlinePaymentRequestDeserializer() {
        return new WeChatPayNativeSpecificOnlinePaymentRequestDeserializer();
    }
}
