package com.thebund1st.tiantong.boot.dummypay

import com.thebund1st.tiantong.boot.AbstractAutoConfigurationTest
import com.thebund1st.tiantong.dummypay.DummyPayOnlinePaymentProviderGateway
import com.thebund1st.tiantong.dummypay.webhooks.DummyPayNotifyPaymentResultCommandAssembler

class DummyPayConfigurationTest extends AbstractAutoConfigurationTest {

    def "it should skip a DummyPayOnlinePaymentProviderGateway instance by default"() {

        when:
        def then = this.contextRunner

        then:
        then.run { it ->
            assert it.getBeanNamesForType(DummyPayOnlinePaymentProviderGateway).length == 0
            assert it.getBeanNamesForType(DummyPayProperties).length == 0
            assert it.getBeanNamesForType(DummyPayNotifyPaymentResultCommandAssembler).length == 0
        }
    }

    def "it should provide a DummyPayOnlinePaymentProviderGateway instance"() {

        when:
        def then = this.contextRunner
                .withPropertyValues("tiantong.dummypay.enabled=true")

        then:
        then.run { it ->
            assert it.getBeanNamesForType(DummyPayOnlinePaymentProviderGateway).length == 1
            assert it.getBeanNamesForType(DummyPayProperties).length == 1
            assert it.getBeanNamesForType(DummyPayNotifyPaymentResultCommandAssembler).length == 1
        }
    }
}
