package foo.bar

import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand
import static io.restassured.RestAssured.given
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class OnlinePaymentAcceptanceTest extends Specification {

    @LocalServerPort
    int port

    def given = given()

    void setup() {
        RestAssured.port = port
        // @formatter:off
        given
            .port(port)
            .log().everything()
            .contentType(APPLICATION_JSON_UTF8_VALUE)
        // @formatter:on
    }

    def "I want to launch an online payment request to my favorite online payment provider"() {
        given:
        def command = aRequestOnlinePaymentCommand()
                .byDummy()
                .withDummySpecificInfo()
                .build()

        when:
        // @formatter:off
        def then = given
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
        // @formatter:on
        then:
        then.statusCode(OK.value())
    }


}
