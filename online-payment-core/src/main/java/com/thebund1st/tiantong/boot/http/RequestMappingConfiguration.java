package com.thebund1st.tiantong.boot.http;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestMappingConfiguration {

    @ConfigurationProperties(prefix = "tiantong.http.request.mapping")
    @Bean
    public RequestMappingProperties requestMappingProperties() {
        return new RequestMappingProperties();
    }

}
