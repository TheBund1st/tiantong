package foo.bar.steps

import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand
import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.dummypay.DummyPayOnlinePaymentProviderGateway
import foo.bar.DomainEventPublisherStub
import io.restassured.response.ValidatableResponse

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand
import static io.restassured.RestAssured.given
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE

class Customer {
    DomainEventPublisherStub domainEventPublisherStub
    DummyPayOnlinePaymentProviderGateway dummyPayOnlinePaymentProviderGateway
    ValidatableResponse currentResponse
    RequestOnlinePaymentCommand currentRequestOnlinePaymentCommand
    String currentOnlinePaymentId

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
                    "providerSpecificRequest": {
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
                    "result": "SUCCESS"
                }
            """)
        .when()
            .post("/webhook/dummypay/payment")
        .then()
            .log().everything()
        //@formatter:on
        this
    }

    def finishPaymentWithDummyPayButWeDontReceiveNotification() {
        dummyPayOnlinePaymentProviderGateway
                .addSucceededOnlinePayment(OnlinePayment.Identifier.of(currentOnlinePaymentId))
        this
    }

    def thenTheOnlinePaymentRequestIsSuccess() {
        this.currentResponse.statusCode(OK.value())
        assert this.currentResponse
                .extract().body()
                .jsonPath().get("result") == "OK"
        thenTheOnlinePaymentIsSucceeded()
    }

    def thenTheOnlinePaymentIsSucceeded() {
        assert domainEventPublisherStub.shouldReceivePaymentSucceedEvent(currentOnlinePaymentId).isPresent()
        this
    }

    def thenTheOnlinePaymentResultIsPulledAndTheOnlinePaymentIsSucceeded() {
        this.currentResponse.statusCode(CREATED.value())
        thenTheOnlinePaymentIsSucceeded()
    }

    def thenTheOnlinePaymentResultIsPulledAutomaticallyAndTheOnlinePaymentIsSucceeded() {
        thenTheOnlinePaymentIsSucceeded()
    }

    def tryPaymentResultSynchronization() {
        //@formatter:off
        this.currentResponse = aGiven()
            .post("/api/online/payments/${currentOnlinePaymentId}/resultSynchronizations")
        .then()
            .log().everything()
        //@formatter:on
        this
    }

    def waitForPaymentResultSynchronization() {
        Thread.sleep(4000)
        this
    }

    def theOnlinePaymentIsClosed() {
        //@formatter:off
        this.currentResponse = aGiven()
            .get("/api/online/payments/${currentOnlinePaymentId}")
        .then()
            .log().everything()
        .statusCode(OK.value())
        assert this.currentResponse
                .extract().body()
                .jsonPath().get("status") == "CLOSED"
        //@formatter:on
        this
    }
}
