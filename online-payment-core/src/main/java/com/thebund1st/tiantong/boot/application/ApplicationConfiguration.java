package com.thebund1st.tiantong.boot.application;

import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ApplicationConfiguration {
    private final OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator;
    private final OnlinePaymentRepository onlinePaymentRepository;
    private final Clock clock;


    @Bean
    public RequestOnlinePaymentCommandHandler requestOnlinePaymentCommandHandler() {
        return new RequestOnlinePaymentCommandHandler(onlinePaymentIdentifierGenerator,
                onlinePaymentRepository, clock);
    }
}
