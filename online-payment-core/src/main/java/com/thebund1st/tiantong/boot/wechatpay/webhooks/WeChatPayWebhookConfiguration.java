package com.thebund1st.tiantong.boot.wechatpay.webhooks;

import com.thebund1st.tiantong.application.NotifyPaymentResultCommandHandler;
import com.thebund1st.tiantong.boot.wechatpay.WeChatPayProperties;
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyPaymentResultCommandAssembler;
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayPaymentResultNotificationWebhookEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WeChatPayWebhookConfiguration {
    @Autowired
    private WeChatPayNotifyPaymentResultCommandAssembler weChatPayNotifyPaymentResultCommandAssembler;
    @Autowired
    private NotifyPaymentResultCommandHandler notifyPaymentResultCommandHandler;

    @Bean
    public FilterRegistrationBean<WeChatPayPaymentResultNotificationWebhookEndpoint>
    weChatPayPaymentResultNotificationWebhookEndpoint(WeChatPayProperties weChatPayProperties) {
        FilterRegistrationBean<WeChatPayPaymentResultNotificationWebhookEndpoint> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(weChatPayPaymentResultNotificationWebhookEndpoint());
        registrationBean.addUrlPatterns(weChatPayProperties.getPaymentResultNotificationWebhookEndpointPath());
        return registrationBean;
    }

    private WeChatPayPaymentResultNotificationWebhookEndpoint weChatPayPaymentResultNotificationWebhookEndpoint() {
        return new WeChatPayPaymentResultNotificationWebhookEndpoint(
                weChatPayNotifyPaymentResultCommandAssembler,
                notifyPaymentResultCommandHandler
        );
    }


}
