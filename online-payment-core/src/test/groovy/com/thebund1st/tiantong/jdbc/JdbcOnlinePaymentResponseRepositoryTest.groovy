package com.thebund1st.tiantong.jdbc


import org.springframework.beans.factory.annotation.Autowired

import static com.thebund1st.tiantong.core.OnlinePaymentResponseFixture.anOnlinePaymentResponse

class JdbcOnlinePaymentResponseRepositoryTest extends AbstractJdbcTest {

    @Autowired
    private JdbcOnlinePaymentResponseRepository subject


    def "it should save online payment response"() {
        given:
        def model = anOnlinePaymentResponse().build()

        when:
        subject.save(model)

        then:
        def actual = subject.mustFindBy(model.id)
        assert actual != null
        assert actual.id == model.id
        assert actual.onlinePaymentId == model.onlinePaymentId
        assert actual.amount == model.amount
        assert actual.text == model.text
        assert actual.createdAt == model.createdAt
    }

}
