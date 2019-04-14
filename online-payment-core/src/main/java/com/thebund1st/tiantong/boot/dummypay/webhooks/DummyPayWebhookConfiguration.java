package com.thebund1st.tiantong.boot.dummypay.webhooks;

import com.thebund1st.tiantong.application.NotifyPaymentResultCommandHandler;
import com.thebund1st.tiantong.boot.dummypay.DummyPayProperties;
import com.thebund1st.tiantong.dummypay.webhooks.DummyPayNotifyPaymentResultCommandAssembler;
import com.thebund1st.tiantong.dummypay.webhooks.DummyPaymentResultNotificationWebhookEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DummyPayWebhookConfiguration {
    @Autowired
    private DummyPayNotifyPaymentResultCommandAssembler notifyPaymentResultCommandAssembler;
    @Autowired
    private NotifyPaymentResultCommandHandler notifyPaymentResultCommandHandler;
    @Autowired
    private DummyPayProperties dummyPayProperties;

    @Bean
    public FilterRegistrationBean<DummyPaymentResultNotificationWebhookEndpoint>
    dummyPayPaymentResultNotificationWebhookEndpointFilterRegistrationBean() {
        FilterRegistrationBean<DummyPaymentResultNotificationWebhookEndpoint> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(dummyPayPaymentResultNotificationWebhookEndpoint());
        registrationBean.addUrlPatterns(dummyPayProperties.getPaymentResultNotificationWebhookEndpointPath());
        return registrationBean;
    }

    private DummyPaymentResultNotificationWebhookEndpoint dummyPayPaymentResultNotificationWebhookEndpoint() {
        return new DummyPaymentResultNotificationWebhookEndpoint(
                notifyPaymentResultCommandAssembler,
                notifyPaymentResultCommandHandler
        );
    }


}
