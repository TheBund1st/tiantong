package com.thebund1st.tiantong.boot.wechatpay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WeChatPayPropertiesConfiguration {

    @ConfigurationProperties(prefix = "tiantong.wechatpay")
    @Bean
    public WeChatPayProperties weChatPayProperties() {
        return new WeChatPayProperties();
    }


}
