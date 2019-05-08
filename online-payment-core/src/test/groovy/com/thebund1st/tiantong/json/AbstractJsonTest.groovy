package com.thebund1st.tiantong.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.thebund1st.tiantong.boot.json.JsonConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.context.annotation.Import
import spock.lang.Specification

@JsonTest
@Import([
        JsonConfiguration
])
abstract class AbstractJsonTest extends Specification {

    @Autowired
    private ObjectMapper objectMapper

    def setup() {
        JacksonTester.initFields(this, objectMapper)
    }


}
