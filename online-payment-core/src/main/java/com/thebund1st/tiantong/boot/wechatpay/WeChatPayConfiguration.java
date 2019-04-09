package com.thebund1st.tiantong.boot.wechatpay;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.thebund1st.tiantong.wechatpay.IpAddressExtractor;
import com.thebund1st.tiantong.wechatpay.NonceGenerator;
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WeChatPayConfiguration {

    @ConfigurationProperties(prefix = "tiantong.wechatpay")
    @Bean
    public WeChatPayProperties weChatPayProperties() {
        return new WeChatPayProperties();
    }

    @Bean
    public NonceGenerator nonceGenerator() {
        return new NonceGenerator();
    }

    @Bean
    public IpAddressExtractor ipAddressExtractor() {
        return new IpAddressExtractor();
    }

    @Bean
    public WxPayService wxPayService() {
        WeChatPayProperties weChatPayProperties = weChatPayProperties();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(weChatPayProperties.getAppId());
        payConfig.setMchId(weChatPayProperties.getMerchantId());
        payConfig.setMchKey(weChatPayProperties.getMerchantKey());
        payConfig.setUseSandboxEnv(weChatPayProperties.isSandbox());
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    @Bean
    public WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway() {
        WeChatPayProperties weChatPayProperties = weChatPayProperties();
        return new WeChatPayOnlinePaymentGateway(wxPayService(),
                nonceGenerator(),
                ipAddressExtractor(),
                weChatPayProperties.getWebhookEndpoint());
    }


}
