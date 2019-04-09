package foo.bar;

import com.thebund1st.tiantong.application.NotifyPaymentResultCommandHandler;
import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResponse;
import com.thebund1st.tiantong.core.OnlinePaymentResponseIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentResponseRepository;
import com.thebund1st.tiantong.jdbc.JdbcOnlinePaymentResponseRepository;
import com.thebund1st.tiantong.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

@Slf4j
@Configuration
public class WeChatPayConfiguration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public NotifyPaymentResultCommandHandler onlinePaymentNotificationSubscriber(OnlinePaymentRepository onlinePaymentRepository) {
        return new NotifyPaymentResultCommandHandler(onlinePaymentRepository,
                onlinePaymentResponseRepository(),
                onlinePaymentResponseIdentifierGenerator(),
                eventPublisher(),
                clock());
    }

    @Bean
    public OnlinePaymentResponseRepository onlinePaymentResponseRepository() {
        return new JdbcOnlinePaymentResponseRepository(jdbcTemplate);
    }

    @Bean
    public OnlinePaymentResponseIdentifierGenerator onlinePaymentResponseIdentifierGenerator() {
        return () -> OnlinePaymentResponse.Identifier.of(UUID.randomUUID().toString().replace("-", ""));
    }

    @Bean
    public DomainEventPublisher eventPublisher() {
        return event -> log.info(event.toString());
    }

    @Bean
    public Clock clock() {
        return new Clock();
    }


}
