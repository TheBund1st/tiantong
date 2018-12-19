package foo.bar;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.thebund1st.tiantong.application.OnlinePaymentCommandHandler;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.time.Clock;
import com.thebund1st.tiantong.wechatpay.IpAddressExtractor;
import com.thebund1st.tiantong.wechatpay.NonceGenerator;
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway;
import com.thebund1st.tiantong.wechatpay.WeChatPayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatPayConfiguration {

    @ConfigurationProperties(prefix = "tiantong.wechatpay")
    @Bean
    public WeChatPayProperties weChatPayProperties() {
        return new WeChatPayProperties();
    }

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
    public OnlinePaymentCommandHandler onlinePaymentCommandHandler() {
        return new OnlinePaymentCommandHandler(onlinePaymentRepository(), clock());
    }

    @Bean
    public OnlinePaymentRepository onlinePaymentRepository() {
        return new OnlinePaymentRepository();
    }

    @Bean
    public Clock clock() {
        return new Clock();
    }

    @Bean
    public WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway() {
        return new WeChatPayOnlinePaymentGateway(wxPayService(),
                nonceGenerator(), ipAddressExtractor(), "https://fe175340.ap.ngrok.io/api/notifications");
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
