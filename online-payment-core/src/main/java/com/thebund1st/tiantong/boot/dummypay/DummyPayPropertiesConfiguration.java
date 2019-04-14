package com.thebund1st.tiantong.boot.dummypay;

import com.thebund1st.tiantong.boot.dummypay.webhooks.DummyPayWebhookConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@RequiredArgsConstructor
@Import({DummyPayWebhookConfiguration.class})
public class DummyPayPropertiesConfiguration {

    @ConfigurationProperties(prefix = "tiantong.dummypay")
    @Bean
    public DummyPayProperties dummyPayProperties() {
        return new DummyPayProperties();
    }

}
