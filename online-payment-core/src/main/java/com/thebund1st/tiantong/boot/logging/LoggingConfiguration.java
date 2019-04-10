package com.thebund1st.tiantong.boot.logging;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.logging.LoggingDomainEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

    @ConditionalOnMissingBean(name = "domainEventPublisherDelegate")
    @Bean(name = "domainEventPublisherDelegate")
    public DomainEventPublisher loggingDomainEventPublisher() {
        return new LoggingDomainEventPublisher();
    }
}
