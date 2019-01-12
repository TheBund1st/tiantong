package foo.bar;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.thebund1st.tiantong.application.OnlinePaymentNotificationSubscriber;
import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.core.EventPublisher;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResponse;
import com.thebund1st.tiantong.core.OnlinePaymentResponseIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentResponseRepository;
import com.thebund1st.tiantong.jdbc.JdbcOnlinePaymentRepository;
import com.thebund1st.tiantong.jdbc.JdbcOnlinePaymentResponseRepository;
import com.thebund1st.tiantong.time.Clock;
import com.thebund1st.tiantong.wechatpay.IpAddressExtractor;
import com.thebund1st.tiantong.wechatpay.NonceGenerator;
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway;
import com.thebund1st.tiantong.wechatpay.WeChatPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

@Slf4j
@Configuration
public class WeChatPayConfiguration {

    @ConfigurationProperties(prefix = "tiantong.wechatpay")
    @Bean
    public WeChatPayProperties weChatPayProperties() {
        return new WeChatPayProperties();
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public WxPayService wxPayService() {
        WeChatPayProperties weChatPayProperties = weChatPayProperties();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(weChatPayProperties.getAppId());
        payConfig.setMchId(weChatPayProperties.getMerchantId());
        payConfig.setMchKey(weChatPayProperties.getMerchantKey());
        payConfig.setUseSandboxEnv(weChatPayProperties.isSandbox());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;

    }

    @Bean
    public RequestOnlinePaymentCommandHandler requestOnlinePaymentCommandHandler() {
        return new RequestOnlinePaymentCommandHandler(onlinePaymentIdentifierGenerator(),
                onlinePaymentRepository(), clock());
    }

    @Bean
    public OnlinePaymentNotificationSubscriber onlinePaymentNotificationSubscriber() {
        return new OnlinePaymentNotificationSubscriber(onlinePaymentRepository(),
                null,
                onlinePaymentResponseIdentifierGenerator(),
                eventPublisher(),
                clock());
    }

    @Bean
    public OnlinePaymentRepository onlinePaymentRepository() {
        return new JdbcOnlinePaymentRepository(jdbcTemplate);
    }

    @Bean
    public OnlinePaymentResponseRepository onlinePaymentResponseRepository() {
        return new JdbcOnlinePaymentResponseRepository(jdbcTemplate);
    }

    @Bean
    public OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator() {
        return () -> OnlinePayment.Identifier.of(UUID.randomUUID().toString().replace("-", ""));
    }

    @Bean
    public OnlinePaymentResponseIdentifierGenerator onlinePaymentResponseIdentifierGenerator() {
        return () -> OnlinePaymentResponse.Identifier.of(UUID.randomUUID().toString().replace("-", ""));
    }

    @Bean
    public EventPublisher eventPublisher() {
        return event -> log.info(event.toString());
    }

    @Bean
    public Clock clock() {
        return new Clock();
    }

    @Bean
    public WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway() {
        return new WeChatPayOnlinePaymentGateway(wxPayService(),
                nonceGenerator(), ipAddressExtractor(), "https://b9abf77f.ap.ngrok.io/api/notifications");
    }

    @Bean
    public NonceGenerator nonceGenerator() {
        return new NonceGenerator();
    }

    @Bean
    public IpAddressExtractor ipAddressExtractor() {
        return new IpAddressExtractor();
    }
}
