package foo.bar


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

    private Customer customer

    void setup() {
        RestAssured.port = port
        customer = new Customer(domainEventPublisherStub)
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


}
