package com.thebund1st.tiantong.boot.dummypay.webhooks;

import com.thebund1st.tiantong.application.NotifyOnlinePaymentResultCommandHandler;
import com.thebund1st.tiantong.boot.dummypay.DummyPayProperties;
import com.thebund1st.tiantong.dummypay.webhooks.DummyPayNotifyOnlinePaymentResultCommandAssembler;
import com.thebund1st.tiantong.dummypay.webhooks.DummyPayNotifyOnlinePaymentResultResponseBodyAssembler;
import com.thebund1st.tiantong.web.webhooks.NotifyOnlinePaymentResultWebhookEndpoint;
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
    private DummyPayNotifyOnlinePaymentResultCommandAssembler notifyPaymentResultCommandAssembler;
    @Autowired
    private NotifyOnlinePaymentResultCommandHandler notifyOnlinePaymentResultCommandHandler;
    @Autowired
    private DummyPayNotifyOnlinePaymentResultResponseBodyAssembler dummyPayNotifyOnlinePaymentResultResponseBodyAssembler;
    @Autowired
    private DummyPayProperties dummyPayProperties;

    @Bean
    public FilterRegistrationBean<NotifyOnlinePaymentResultWebhookEndpoint>
    dummyPayPaymentResultNotificationWebhookEndpointFilterRegistrationBean() {
        FilterRegistrationBean<NotifyOnlinePaymentResultWebhookEndpoint> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(dummyPayPaymentResultNotificationWebhookEndpoint());
        registrationBean.addUrlPatterns(dummyPayProperties.getPaymentResultNotificationWebhookEndpointPath());
        return registrationBean;
    }

    private NotifyOnlinePaymentResultWebhookEndpoint dummyPayPaymentResultNotificationWebhookEndpoint() {
        return new NotifyOnlinePaymentResultWebhookEndpoint(
                notifyPaymentResultCommandAssembler,
                notifyOnlinePaymentResultCommandHandler,
                dummyPayNotifyOnlinePaymentResultResponseBodyAssembler
        );
    }


}
