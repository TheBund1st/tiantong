package com.thebund1st.tiantong.jdbc


import org.springframework.beans.factory.annotation.Autowired

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class JdbcOnlinePaymentRepositoryTest extends AbstractJdbcTest {

    @Autowired
    private JdbcOnlinePaymentRepository subject


    def "it should save op"() {
        given:
        def op = anOnlinePayment().build()

        when:
        subject.save(op)

        then:
        def actual = subject.mustFindBy(op.id)
        assert actual != null
        assert actual.id == op.id
        assert actual.version == op.version
        assert actual.amount == op.amount
        assert actual.correlation == op.correlation
        assert actual.method == op.method
        assert actual.status == op.status
        assert actual.createdAt == op.createdAt
        assert actual.lastModifiedAt == op.lastModifiedAt
        assert actual.subject == op.subject
    }

}
