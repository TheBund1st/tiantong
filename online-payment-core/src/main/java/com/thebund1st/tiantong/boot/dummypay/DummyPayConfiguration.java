package com.thebund1st.tiantong.boot.dummypay;

import com.thebund1st.tiantong.boot.dummypay.webhooks.DummyPayWebhookConfiguration;
import com.thebund1st.tiantong.dummypay.DummyPayOnlinePaymentProviderGateway;
import com.thebund1st.tiantong.dummypay.webhooks.DummyPayNotifyPaymentResultCommandAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@RequiredArgsConstructor
@Import({DummyPayWebhookConfiguration.class, DummyPayPropertiesConfiguration.class})
public class DummyPayConfiguration {


    @Bean
    public DummyPayOnlinePaymentProviderGateway dummyOnlinePaymentProviderGateway() {
        return new DummyPayOnlinePaymentProviderGateway();
    }

    @Bean
    public DummyPayNotifyPaymentResultCommandAssembler dummyNotifyPaymentResultCommandAssembler() {
        return new DummyPayNotifyPaymentResultCommandAssembler();
    }
}
