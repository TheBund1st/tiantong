package com.thebund1st.tiantong.web

import com.thebund1st.tiantong.application.NotifyPaymentResultCommandHandler
import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler
import com.thebund1st.tiantong.application.RequestOnlineRefundCommandHandler
import com.thebund1st.tiantong.application.SyncOnlinePaymentResultCommandHandler
import com.thebund1st.tiantong.boot.dummypay.DummyPayPropertiesConfiguration
import com.thebund1st.tiantong.boot.dummypay.webhooks.DummyPayWebhookConfiguration
import com.thebund1st.tiantong.boot.http.RequestMappingConfiguration
import com.thebund1st.tiantong.boot.json.JsonConfiguration
import com.thebund1st.tiantong.boot.wechatpay.WeChatPayPropertiesConfiguration
import com.thebund1st.tiantong.boot.wechatpay.webhooks.WeChatPayWebhookConfiguration
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway
import com.thebund1st.tiantong.core.OnlineRefundProviderGateway
import com.thebund1st.tiantong.dummypay.webhooks.DummyPayNotifyPaymentResultCommandAssembler
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyPaymentResultCommandAssembler
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@WebMvcTest
@Import([
        WeChatPayWebhookConfiguration, WeChatPayPropertiesConfiguration,
        DummyPayWebhookConfiguration, DummyPayPropertiesConfiguration,
        JsonConfiguration, RequestMappingConfiguration
])
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
    protected DummyPayNotifyPaymentResultCommandAssembler dummyNotifyPaymentResultCommandAssembler = Mock()

    @SpringBean
    protected NotifyPaymentResultCommandHandler onlinePaymentNotificationSubscriber = Mock()

    @SpringBean
    protected RequestOnlineRefundCommandHandler requestOnlineRefundCommandHandler = Mock()

    @SpringBean
    protected OnlineRefundProviderGateway onlineRefundProviderGateway = Mock()

    @SpringBean
    protected SyncOnlinePaymentResultCommandHandler syncOnlinePaymentResultCommandHandler = Mock()

    def setup() {
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    protected def given() {
        RestAssuredMockMvc.given()
    }
}
