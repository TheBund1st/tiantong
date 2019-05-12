package com.thebund1st.tiantong.boot;

import com.thebund1st.tiantong.boot.amqp.AmqpConfiguration;
import com.thebund1st.tiantong.boot.application.ApplicationConfiguration;
import com.thebund1st.tiantong.boot.core.CoreConfiguration;
import com.thebund1st.tiantong.boot.dummypay.DummyPayConfiguration;
import com.thebund1st.tiantong.boot.http.RequestMappingConfiguration;
import com.thebund1st.tiantong.boot.jdbc.JdbcConfiguration;
import com.thebund1st.tiantong.boot.json.JsonConfiguration;
import com.thebund1st.tiantong.boot.logging.LoggingConfiguration;
import com.thebund1st.tiantong.boot.provider.ProviderConfiguration;
import com.thebund1st.tiantong.boot.time.TimeConfiguration;
import com.thebund1st.tiantong.boot.wechatpay.WeChatPayConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//TODO figure out is @ComponentScan is a good practice or not?
@ComponentScan(basePackages = {
        "com.thebund1st.tiantong.web",
})
@Import({
        RequestMappingConfiguration.class,
        JsonConfiguration.class,
        AmqpConfiguration.class,
        DummyPayConfiguration.class,
        WeChatPayConfiguration.class,
        LoggingConfiguration.class,
        JdbcConfiguration.class,
        ProviderConfiguration.class,
        TimeConfiguration.class,
        CoreConfiguration.class,
        ApplicationConfiguration.class
})
@Configuration
public class OnlinePaymentAutoConfiguration {


}