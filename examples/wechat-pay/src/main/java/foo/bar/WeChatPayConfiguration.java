package foo.bar;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WeChatPayConfiguration {

    @Bean
    public DomainEventPublisher eventPublisher() {
        return event -> log.info(event.toString());
    }


}
