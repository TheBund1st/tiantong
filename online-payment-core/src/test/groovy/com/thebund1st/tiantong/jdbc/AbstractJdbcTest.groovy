package com.thebund1st.tiantong.jdbc

import com.thebund1st.tiantong.boot.jdbc.JdbcConfiguration
import com.thebund1st.tiantong.boot.json.JsonConfiguration
import com.thebund1st.tiantong.boot.logging.LoggingConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED

@Import([
        JdbcConfiguration,
        LoggingConfiguration,
        JsonConfiguration,
        JacksonAutoConfiguration
])
@Transactional(propagation = NOT_SUPPORTED)
@DataJpaTest
class AbstractJdbcTest extends Specification {


}
