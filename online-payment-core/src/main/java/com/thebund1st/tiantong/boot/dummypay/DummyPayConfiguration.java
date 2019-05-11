package com.thebund1st.tiantong.boot.dummypay;

import com.thebund1st.tiantong.boot.dummypay.webhooks.DummyPayWebhookConfiguration;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator;
import com.thebund1st.tiantong.dummypay.DummyPayOnlinePaymentProviderGateway;
import com.thebund1st.tiantong.dummypay.webhooks.DummyPayNotifyPaymentResultCommandAssembler;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnProperty(prefix = "tiantong.dummypay", name = "enabled", havingValue = "true")
@Configuration
@RequiredArgsConstructor
@Import({DummyPayWebhookConfiguration.class, DummyPayPropertiesConfiguration.class})
public class DummyPayConfiguration {

    @Autowired
    private Clock clock;

    @Autowired
    private OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResultNotificationIdentifierGenerator;

    @Bean
    public DummyPayOnlinePaymentProviderGateway dummyOnlinePaymentProviderGateway() {
        return new DummyPayOnlinePaymentProviderGateway(clock, onlinePaymentResultNotificationIdentifierGenerator);
    }

    @Bean
    public DummyPayNotifyPaymentResultCommandAssembler dummyNotifyPaymentResultCommandAssembler() {
        return new DummyPayNotifyPaymentResultCommandAssembler();
    }
}
