package foo.bar

import com.thebund1st.tiantong.dummypay.DummyPayOnlinePaymentProviderGateway
import foo.bar.steps.Customer
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class OnlinePaymentAcceptanceTest extends Specification {

    @LocalServerPort
    int port

    @Autowired
    private DomainEventPublisherStub domainEventPublisherStub

    @Autowired
    private DummyPayOnlinePaymentProviderGateway dummyPayOnlinePaymentProviderGateway

    private Customer customer

    void setup() {
        RestAssured.port = port
        customer = new Customer(
                domainEventPublisherStub: domainEventPublisherStub,
                dummyPayOnlinePaymentProviderGateway: dummyPayOnlinePaymentProviderGateway
        )
    }

    def "I want to launch an online payment request to my favorite online payment provider"() {
        when:
        customer.requestPaymentToDummyPay()

        then:
        customer.thenTheRequestIsSentToTheOnlinePaymentProvider()
    }

    def "I want to finish payment to close the online payment request"() {
        given:
        customer.requestPaymentToDummyPay()
        customer.thenTheRequestIsSentToTheOnlinePaymentProvider()

        when:
        customer.finishPaymentWithDummyPay()

        then:
        customer.thenTheOnlinePaymentRequestIsSuccess()
    }

    def "The payment result can be synchronized to finish payment"() {
        given:
        customer.requestPaymentToDummyPay()
        customer.thenTheRequestIsSentToTheOnlinePaymentProvider()
        customer.finishPaymentWithDummyPayButWeDontReceiveNotification()

        when:
        customer.tryPaymentResultSynchronization()

        then:
        customer.thenTheOnlinePaymentResultIsPulledAndTheOnlinePaymentIsSucceeded()
    }


}
