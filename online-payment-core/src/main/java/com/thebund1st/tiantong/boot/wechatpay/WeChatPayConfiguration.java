package com.thebund1st.tiantong.boot.wechatpay;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.thebund1st.tiantong.boot.wechatpay.webhooks.WeChatPayWebhookConfiguration;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator;
import com.thebund1st.tiantong.time.Clock;
import com.thebund1st.tiantong.wechatpay.IpAddressExtractor;
import com.thebund1st.tiantong.wechatpay.NonceGenerator;
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway;
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator;
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiLaunchOnlinePaymentRequestAssembler;
import com.thebund1st.tiantong.wechatpay.payment.WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulatorDispatcher;
import com.thebund1st.tiantong.wechatpay.payment.WeChatPayLaunchOnlinePaymentRequestAssemblerDispatcher;
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator;
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeLaunchOnlinePaymentRequestAssembler;
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyOnlinePaymentResultCommandAssembler;
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyOnlinePaymentResultResponseBodyAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static java.util.Arrays.asList;

@Slf4j
@Configuration
@Import({WeChatPayWebhookConfiguration.class, WeChatPayPropertiesConfiguration.class})
public class WeChatPayConfiguration {

    @Autowired
    private OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResultNotificationIdentifierGenerator;

    @Autowired
    private Clock clock;

    @Bean
    public NonceGenerator nonceGenerator() {
        return new NonceGenerator();
    }

    @Bean
    public IpAddressExtractor ipAddressExtractor() {
        return new IpAddressExtractor();
    }

    @Bean
    public WxPayService wxPayService(WeChatPayProperties weChatPayProperties) {
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
    public WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulatorDispatcher weChatPayCreateOrderRequestPopulatorDispatcher() {
        return
                new WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulatorDispatcher(asList(
                        new WeChatPayNativeCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator(),
                        new WeChatPayJsApiCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator())
                );
    }

    @Bean
    public WeChatPayLaunchOnlinePaymentRequestAssemblerDispatcher
    weChatPayProviderSpecificUserAgentRequestAssemblerDispatcher(WxPayService wxPayService) {
        return
                new WeChatPayLaunchOnlinePaymentRequestAssemblerDispatcher(asList(
                        new WeChatPayNativeLaunchOnlinePaymentRequestAssembler(),
                        new WeChatPayJsApiLaunchOnlinePaymentRequestAssembler(nonceGenerator(), clock, wxPayService))
                );
    }

    @Bean
    public WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway(WeChatPayProperties weChatPayProperties) {
        WxPayService wxPayService = wxPayService(weChatPayProperties);
        return new WeChatPayOnlinePaymentGateway(wxPayService,
                nonceGenerator(),
                ipAddressExtractor(),
                weChatPayProperties.paymentResultNotificationWebhookEndpointUri(),
                weChatPayProperties.refundResultNotificationWebhookEndpointUri(),
                weChatPayCreateOrderRequestPopulatorDispatcher(),
                weChatPayProviderSpecificUserAgentRequestAssemblerDispatcher(wxPayService),
                onlinePaymentResultNotificationIdentifierGenerator,
                clock
        );
    }

    @Bean
    public WeChatPayNotifyOnlinePaymentResultCommandAssembler weChatPayNotifyPaymentResultCommandAssembler(
            WxPayService wxPayService) {
        return new WeChatPayNotifyOnlinePaymentResultCommandAssembler(wxPayService);
    }

    @Bean
    public WeChatPayNotifyOnlinePaymentResultResponseBodyAssembler weChatPayNotifyOnlinePaymentResultResponseBodyAssembler() {
        return new WeChatPayNotifyOnlinePaymentResultResponseBodyAssembler();
    }

}
