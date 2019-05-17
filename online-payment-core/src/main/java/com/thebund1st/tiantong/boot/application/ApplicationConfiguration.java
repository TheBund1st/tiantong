package com.thebund1st.tiantong.boot.application;

import com.thebund1st.tiantong.application.NotifyOnlinePaymentResultCommandHandler;
import com.thebund1st.tiantong.application.CreateOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.application.RequestOnlineRefundCommandHandler;
import com.thebund1st.tiantong.application.SyncOnlinePaymentResultCommandHandler;
import com.thebund1st.tiantong.boot.application.scheduling.SchedulingConfiguration;
import com.thebund1st.tiantong.boot.core.OnlinePaymentResultSynchronizationProperties;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationRepository;
import com.thebund1st.tiantong.core.refund.OnlineRefundIdentifierGenerator;
import com.thebund1st.tiantong.core.refund.OnlineRefundRepository;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@RequiredArgsConstructor
@Configuration
@Import(SchedulingConfiguration.class)
public class ApplicationConfiguration {
    private final OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator;
    private final OnlinePaymentRepository onlinePaymentRepository;
    private final OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResultNotificationIdentifierGenerator;
    private final OnlinePaymentResultNotificationRepository onlinePaymentResultNotificationRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final Clock clock;
    private final OnlineRefundIdentifierGenerator onlineRefundIdentifierGenerator;
    private final OnlineRefundRepository onlineRefundRepository;
    private final OnlinePaymentResultGateway onlinePaymentResultGateway;
    private final ProviderSpecificCloseOnlinePaymentGateway providerSpecificCloseOnlinePaymentGateway;
    @Autowired
    private OnlinePaymentResultSynchronizationProperties onlinePaymentResultSynchronizationProperties;


    @Bean
    public CreateOnlinePaymentCommandHandler requestOnlinePaymentCommandHandler() {
        CreateOnlinePaymentCommandHandler handler = new CreateOnlinePaymentCommandHandler(onlinePaymentIdentifierGenerator,
                onlinePaymentRepository, clock);
        handler.setExpires(onlinePaymentResultSynchronizationProperties.getExpires());
        return handler;
    }

    @Bean
    public NotifyOnlinePaymentResultCommandHandler onlinePaymentNotificationSubscriber() {
        return new NotifyOnlinePaymentResultCommandHandler(onlinePaymentRepository,
                onlinePaymentResultNotificationRepository,
                onlinePaymentResultNotificationIdentifierGenerator,
                domainEventPublisher,
                clock);
    }

    @Bean
    public SyncOnlinePaymentResultCommandHandler syncOnlinePaymentResultCommandHandler() {
        return new SyncOnlinePaymentResultCommandHandler(
                onlinePaymentRepository,
                onlinePaymentResultGateway,
                onlinePaymentNotificationSubscriber(),
                providerSpecificCloseOnlinePaymentGateway,
                clock);
    }

    @Bean
    public RequestOnlineRefundCommandHandler requestOnlineRefundCommandHandler() {
        return new RequestOnlineRefundCommandHandler(onlinePaymentRepository,
                onlineRefundIdentifierGenerator, onlineRefundRepository, clock);
    }

}
