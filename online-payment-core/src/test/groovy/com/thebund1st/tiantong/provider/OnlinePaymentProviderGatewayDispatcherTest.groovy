package com.thebund1st.tiantong.provider

import com.thebund1st.tiantong.core.DummyProviderSpecificRequest
import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException
import spock.lang.Specification

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class OnlinePaymentProviderGatewayDispatcherTest extends Specification {

    @SuppressWarnings("GroovyAssignabilityCheck")
    private MethodBasedOnlinePaymentProviderGateway dummyGateway = Mock(name: 'dummy')
    @SuppressWarnings("GroovyAssignabilityCheck")
    private MethodBasedOnlinePaymentProviderGateway anotherDummyGateway = Mock(name: 'anotherDummy')
    private OnlinePaymentProviderGatewayDispatcher subject

    def setup() {
        def delegates = [:]
        delegates[OnlinePayment.Method.of("DUMMY")] = dummyGateway
        delegates[OnlinePayment.Method.of("ANOTHER_DUMMY")] = anotherDummyGateway
        subject = new OnlinePaymentProviderGatewayDispatcher(delegates)
    }

    def "it should dispatch online payment to corresponding provider gateway"() {

        given:
        def onlinePayment = anOnlinePayment().byMethod("DUMMY").build()

        and:
        def request = new DummyProviderSpecificRequest(dummyId: "dummyId")
        dummyGateway.request(onlinePayment) >> request

        when:
        def actual = subject.request(onlinePayment)

        then:
        assert request == actual
    }

    def "it should dispatch online payment to another provider gateway"() {

        given:
        def onlinePayment = anOnlinePayment().byMethod("ANOTHER_DUMMY").build()

        and:
        def request = new DummyProviderSpecificRequest(dummyId: "anotherDummyId")
        anotherDummyGateway.request(onlinePayment) >> request

        when:
        def actual = subject.request(onlinePayment)

        then:
        assert request == actual
    }

    def "it should throw given no corresponding provider gateway"() {

        given:
        def onlinePayment = anOnlinePayment().idIs("1").byMethod("WHO_AM_I").build()

        when:
        subject.request(onlinePayment)

        then:
        def exception = thrown(NoSuchOnlinePaymentProviderGatewayException)
        assert exception != null
        assert exception.getMessage() ==
                "Cannot find OnlinePaymentProviderGateway for OnlinePayment[1] with method[WHO_AM_I]"
    }
}
