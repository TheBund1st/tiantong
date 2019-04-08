package com.thebund1st.tiantong.web


import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@WebMvcTest
class AbstractWebMvcTest extends Specification {

    @Autowired
    protected MockMvc mockMvc

    @SpringBean
    protected RequestOnlinePaymentCommandHandler requestOnlinePaymentCommandHandler = Mock()

    @SpringBean
    protected OnlinePaymentProviderGateway onlinePaymentProviderGateway = Mock()

}
