package foo.bar

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Ignore
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
@Ignore
//FIXME remove ignore once database migration solution is determined
class ApplicationContextSmokeTest extends Specification {

    def "it should load application context"() {
        when: "it launch the application context"

        then: "it should load successfully"
    }
}
