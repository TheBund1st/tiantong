package com.thebund1st.tiantong.boot.application;

import com.thebund1st.tiantong.application.CloseOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.application.NotifyPaymentResultCommandHandler;
import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.application.RequestOnlineRefundCommandHandler;
import com.thebund1st.tiantong.application.SyncOnlinePaymentResultCommandHandler;
import com.thebund1st.tiantong.boot.core.OnlinePaymentResultSynchronizationProperties;
import com.thebund1st.tiantong.boot.application.scheduling.SchedulingConfiguration;
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
    @Autowired
    private OnlinePaymentResultSynchronizationProperties onlinePaymentResultSynchronizationProperties;


    @Bean
    public RequestOnlinePaymentCommandHandler requestOnlinePaymentCommandHandler() {
        RequestOnlinePaymentCommandHandler handler = new RequestOnlinePaymentCommandHandler(onlinePaymentIdentifierGenerator,
                onlinePaymentRepository, clock);
        handler.setExpires(onlinePaymentResultSynchronizationProperties.getExpires());
        return handler;
    }

    @Bean
    public NotifyPaymentResultCommandHandler onlinePaymentNotificationSubscriber() {
        return new NotifyPaymentResultCommandHandler(onlinePaymentRepository,
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
                closeOnlinePaymentCommandHandler());
    }

    @Bean
    public RequestOnlineRefundCommandHandler requestOnlineRefundCommandHandler() {
        return new RequestOnlineRefundCommandHandler(onlinePaymentRepository,
                onlineRefundIdentifierGenerator, onlineRefundRepository, clock);
    }

    @Bean
    public CloseOnlinePaymentCommandHandler closeOnlinePaymentCommandHandler() {
        CloseOnlinePaymentCommandHandler handler = new CloseOnlinePaymentCommandHandler(onlinePaymentRepository,
                domainEventPublisher,
                clock);
        handler.setKeep(onlinePaymentResultSynchronizationProperties.getExpires());
        return handler;
    }
}
