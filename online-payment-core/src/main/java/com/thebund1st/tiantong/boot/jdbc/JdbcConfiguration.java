package com.thebund1st.tiantong.boot.jdbc;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.jdbc.JdbcOnlinePaymentRepository;
import com.thebund1st.tiantong.jdbc.JdbcOnlinePaymentResultNotificationRepository;
import com.thebund1st.tiantong.jdbc.JdbcOnlineRefundRepository;
import com.thebund1st.tiantong.jdbc.PostTransactionCommitDomainEventPublisher;
import com.thebund1st.tiantong.json.deserializers.ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher;
import com.thebund1st.tiantong.json.serializers.ProviderSpecificOnlinePaymentRequestJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcConfiguration {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProviderSpecificOnlinePaymentRequestJsonSerializer providerSpecificOnlinePaymentRequestJsonSerializer;

    @Autowired
    private ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher providerSpecificOnlinePaymentRequestJsonDeserializer;

    @Bean
    public JdbcOnlinePaymentRepository jdbcOnlinePaymentRepository() {
        return new JdbcOnlinePaymentRepository(jdbcTemplate,
                providerSpecificOnlinePaymentRequestJsonSerializer,
                providerSpecificOnlinePaymentRequestJsonDeserializer);
    }

    @Bean
    public JdbcOnlinePaymentResultNotificationRepository jdbcOnlinePaymentResponseRepository() {
        return new JdbcOnlinePaymentResultNotificationRepository(jdbcTemplate);
    }

    @Bean
    public JdbcOnlineRefundRepository jdbcOnlineRefundRepository() {
        return new JdbcOnlineRefundRepository(jdbcTemplate);
    }

    @Primary
    @Bean
    public PostTransactionCommitDomainEventPublisher postTransactionCommitDomainEventPublisher(
            @Qualifier("domainEventPublisherDelegate") DomainEventPublisher delegate) {
        return new PostTransactionCommitDomainEventPublisher(delegate);
    }
}
