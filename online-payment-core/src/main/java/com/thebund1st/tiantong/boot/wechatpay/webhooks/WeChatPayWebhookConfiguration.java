package com.thebund1st.tiantong.boot.wechatpay.webhooks;

import com.thebund1st.tiantong.application.NotifyOnlinePaymentResultCommandHandler;
import com.thebund1st.tiantong.boot.wechatpay.WeChatPayProperties;
import com.thebund1st.tiantong.web.webhooks.NotifyOnlinePaymentResultWebhookEndpoint;
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyOnlinePaymentResultCommandAssembler;
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyOnlinePaymentResultResponseBodyAssembler;
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
    private WeChatPayNotifyOnlinePaymentResultCommandAssembler weChatPayNotifyPaymentResultCommandAssembler;
    @Autowired
    private NotifyOnlinePaymentResultCommandHandler notifyOnlinePaymentResultCommandHandler;
    @Autowired
    private WeChatPayNotifyOnlinePaymentResultResponseBodyAssembler weChatPayNotifyOnlinePaymentResultResponseBodyAssembler;

    @Bean
    public FilterRegistrationBean<NotifyOnlinePaymentResultWebhookEndpoint>
    weChatPayPaymentResultNotificationWebhookEndpoint(WeChatPayProperties weChatPayProperties) {
        FilterRegistrationBean<NotifyOnlinePaymentResultWebhookEndpoint> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(weChatPayPaymentResultNotificationWebhookEndpoint());
        registrationBean.addUrlPatterns(weChatPayProperties.getPaymentResultNotificationWebhookEndpointPath());
        return registrationBean;
    }

    private NotifyOnlinePaymentResultWebhookEndpoint weChatPayPaymentResultNotificationWebhookEndpoint() {
        return new NotifyOnlinePaymentResultWebhookEndpoint(
                weChatPayNotifyPaymentResultCommandAssembler,
                notifyOnlinePaymentResultCommandHandler,
                weChatPayNotifyOnlinePaymentResultResponseBodyAssembler
        );
    }


}
