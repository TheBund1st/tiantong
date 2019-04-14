package foo.bar;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @ConditionalOnMissingBean(name = "domainEventPublisherDelegate")
    @Bean(name = "domainEventPublisherDelegate")
    public DomainEventPublisherStub domainEventPublisherStub() {
        return new DomainEventPublisherStub();
    }
}
