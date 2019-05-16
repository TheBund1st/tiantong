package com.thebund1st.tiantong.boot.application

import com.thebund1st.tiantong.application.NotifyPaymentResultCommandHandler
import com.thebund1st.tiantong.application.CreateOnlinePaymentCommandHandler
import com.thebund1st.tiantong.boot.AbstractAutoConfigurationTest

class ApplicationConfigurationTest extends AbstractAutoConfigurationTest {

    def "it should provide a RequestOnlinePaymentCommandHandler instance"() {

        when:
        def contextRunner = this.contextRunner

        then:
        contextRunner.run { it ->
            CreateOnlinePaymentCommandHandler actual = it.getBean(CreateOnlinePaymentCommandHandler)
            assert actual != null
        }
    }

    def "it should provide a OnlinePaymentNotificationSubscriber instance"() {

        when:
        def then = this.contextRunner

        then:
        then.run { it ->
            NotifyPaymentResultCommandHandler actual = it.getBean(NotifyPaymentResultCommandHandler)
            assert actual != null
        }
    }
}
