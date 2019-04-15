package foo.bar.steps

import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand
import foo.bar.DomainEventPublisherStub
import io.restassured.response.ValidatableResponse

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand
import static io.restassured.RestAssured.given
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE

class Customer {
    private DomainEventPublisherStub domainEventPublisherStub
    ValidatableResponse currentResponse
    RequestOnlinePaymentCommand currentRequestOnlinePaymentCommand
    String currentOnlinePaymentId

    Customer(DomainEventPublisherStub domainEventPublisherStub) {
        this.domainEventPublisherStub = domainEventPublisherStub
    }

    def requestPayment(command) {
        //@formatter:off
        this.currentResponse = aGiven()
                .body("""
                {
                    "amount": "${command.getAmount()}",
                    "method": "${command.getMethod()}",
                    "correlation": {
                        "key":"${command.getCorrelation().getKey()}",
                        "value": "${command.getCorrelation().getValue()}"
                    },
                    "providerSpecificInfo": {
                        "dummy": "dummy"
                    },
                    "subject": "${command.getSubject()}",
                    "body": "${command.getBody()}"
                }
            """)
            .when()
                .post("/api/online/payments")
            .then()
                .log().everything()
        //@formatter:on
        this.currentRequestOnlinePaymentCommand = command
        this
    }

    def requestPaymentToDummyPay() {
        def command = aRequestOnlinePaymentCommand()
                .byDummy()
                .withDummySpecificInfo()
                .build()
        requestPayment(command)
    }


    static def aGiven() {
        given()
                .log().everything()
                .contentType(APPLICATION_JSON_UTF8_VALUE)
    }

    def thenTheRequestIsSentToTheOnlinePaymentProvider() {
        currentResponse.statusCode(OK.value())
        this.currentOnlinePaymentId = currentResponse.extract()
                .body().jsonPath().getString("identifier")
        this
    }

    def finishPaymentWithDummyPay() {
        //@formatter:off
        this.currentResponse = aGiven()
            .body("""
                {
                    "dummyPaymentId": "${UUID.randomUUID().toString()}",
                    "onlinePaymentId": "${currentOnlinePaymentId}",
                    "amount": ${currentRequestOnlinePaymentCommand.amount},
                    "result": "${currentRequestOnlinePaymentCommand.method}"
                }
            """)
        .when()
            .post("/webhook/dummypay/payment")
        .then()
            .log().everything()
        //@formatter:on
        this
    }

    def thenTheOnlinePaymentRequestIsSuccess() {
        this.currentResponse.statusCode(OK.value())
        assert this.currentResponse
                .extract().body()
                .jsonPath().get("result") == "OK"
        domainEventPublisherStub.shouldReceivePaymentSucceedEvent(currentOnlinePaymentId)
        this
    }
}