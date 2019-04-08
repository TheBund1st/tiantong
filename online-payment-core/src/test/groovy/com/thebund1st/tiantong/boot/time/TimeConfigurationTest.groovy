package com.thebund1st.tiantong.boot.time


import com.thebund1st.tiantong.boot.AbstractAutoConfigurationTest
import com.thebund1st.tiantong.time.Clock

class TimeConfigurationTest extends AbstractAutoConfigurationTest {

    def "it should provide a Clock instance"() {

        when:
        def contextRunner = this.contextRunner

        then:
        contextRunner.run { it ->
            Clock actual = it.getBean(Clock)
            assert actual != null
        }
    }
}
