package com.thebund1st.tiantong.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import spock.lang.Specification

@JsonTest
abstract class AbstractJsonTest extends Specification {

    @Autowired
    private ObjectMapper objectMapper

    def setup() {
        JacksonTester.initFields(this, objectMapper)
    }


}
