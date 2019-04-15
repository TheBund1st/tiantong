package com.thebund1st.tiantong.jdbc


import org.springframework.beans.factory.annotation.Autowired

import static com.thebund1st.tiantong.core.refund.OnlineRefundFixture.anOnlineRefund

class JdbcOnlineRefundRepositoryTest extends AbstractJdbcTest {

    @Autowired
    private JdbcOnlineRefundRepository subject


    def "it should save op"() {
        given:
        def or = anOnlineRefund().build()

        when:
        subject.save(or)

        then:
        def actual = subject.mustFindBy(or.id)
        assert actual != null
        assert actual.id == or.id
        assert actual.version == or.version
        assert actual.amount == or.amount
        assert actual.createdAt == or.createdAt
        assert actual.lastModifiedAt == or.lastModifiedAt
        assert actual.onlinePaymentId == or.onlinePaymentId
        assert actual.onlinePaymentAmount == or.onlinePaymentAmount
    }

}
