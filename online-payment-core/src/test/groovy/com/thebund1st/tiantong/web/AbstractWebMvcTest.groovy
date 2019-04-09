package com.thebund1st.tiantong.web

import com.thebund1st.tiantong.application.NotifyPaymentResultCommandHandler
import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler
import com.thebund1st.tiantong.boot.wechatpay.WeChatPayPropertiesConfiguration
import com.thebund1st.tiantong.boot.wechatpay.webhooks.WeChatPayWebhookConfiguration
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyPaymentResultCommandAssembler
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@WebMvcTest
@Import([WeChatPayWebhookConfiguration, WeChatPayPropertiesConfiguration])
class AbstractWebMvcTest extends Specification {

    @Autowired
    protected MockMvc mockMvc

    @SpringBean
    protected RequestOnlinePaymentCommandHandler requestOnlinePaymentCommandHandler = Mock()

    @SpringBean
    protected OnlinePaymentProviderGateway onlinePaymentProviderGateway = Mock()

    @SpringBean
    protected WeChatPayNotifyPaymentResultCommandAssembler weChatPayNotifyPaymentResultCommandAssembler = Mock()

    @SpringBean
    protected NotifyPaymentResultCommandHandler onlinePaymentNotificationSubscriber = Mock()

}
