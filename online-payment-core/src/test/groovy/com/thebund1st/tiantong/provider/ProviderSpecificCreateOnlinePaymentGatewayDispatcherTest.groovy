package com.thebund1st.tiantong.provider

import com.thebund1st.tiantong.core.method.Method
import com.thebund1st.tiantong.core.payment.FlattenedProviderSpecificCreateOnlinePaymentRequest
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException
import com.thebund1st.tiantong.dummypay.DummyPayCreateOnlinePaymentRequest
import com.thebund1st.tiantong.dummypay.DummyPayLaunchOnlinePaymentRequest
import spock.lang.Specification

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.dummypay.DummyPayMethods.dummyPay

class ProviderSpecificCreateOnlinePaymentGatewayDispatcherTest extends Specification {

    @SuppressWarnings("GroovyAssignabilityCheck")
    private MethodBasedCreateOnlinePaymentGateway dummyGateway = Mock(name: 'dummy')
    @SuppressWarnings("GroovyAssignabilityCheck")
    private MethodBasedCreateOnlinePaymentGateway anotherDummyGateway = Mock(name: 'anotherDummy')
    private ProviderSpecificCreateOnlinePaymentGatewayDispatcher subject

    def setup() {
        def delegates = [dummyGateway, anotherDummyGateway]
        subject = new ProviderSpecificCreateOnlinePaymentGatewayDispatcher(delegates)

        dummyGateway.supports(dummyPay()) >> true
        anotherDummyGateway.supports(Method.of("ANOTHER_DUMMY")) >> true
    }

    def "it should dispatch online payment to corresponding provider gateway"() {

        given:
        def onlinePayment = anOnlinePayment().by(dummyPay()).build()
        def providerSpecificRequest = new DummyPayCreateOnlinePaymentRequest(dummy: 'foo')

        and:
        def request = new DummyPayLaunchOnlinePaymentRequest(dummyId: "dummyId")
        dummyGateway.create(onlinePayment, providerSpecificRequest) >> request

        when:
        def actual = subject.create(onlinePayment, providerSpecificRequest)

        then:
        assert request == actual
    }

    def "it should dispatch online payment to another provider gateway"() {

        given:
        def onlinePayment = anOnlinePayment().byMethod("ANOTHER_DUMMY").build()
        def providerSpecificRequest = new FlattenedProviderSpecificCreateOnlinePaymentRequest()

        and:
        def request = new DummyPayLaunchOnlinePaymentRequest(dummyId: "anotherDummyId")
        anotherDummyGateway.create(onlinePayment, providerSpecificRequest) >> request

        when:
        def actual = subject.create(onlinePayment, providerSpecificRequest)

        then:
        assert request == actual
    }

    def "it should throw given no corresponding provider gateway"() {

        given:
        def onlinePayment = anOnlinePayment().idIs("1").byMethod("WHO_AM_I").build()
        def providerSpecificRequest = new FlattenedProviderSpecificCreateOnlinePaymentRequest()

        when:
        subject.create(onlinePayment, providerSpecificRequest)

        then:
        def exception = thrown(NoSuchOnlinePaymentProviderGatewayException)
        assert exception != null
        assert exception.getMessage() ==
                "Cannot find ProviderSpecificCreateOnlinePaymentGateway for OnlinePayment[1] with method[WHO_AM_I]"
    }
}
