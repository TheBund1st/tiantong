package com.thebund1st.tiantong.jdbc

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment
import static com.thebund1st.tiantong.core.OnlinePaymentResultNotification.Code.SUCCESS

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
        assert actual.body == op.body
        assert actual.providerSpecificInfo == op.providerSpecificInfo
    }

    def "it should update op"() {
        given:
        def op = anOnlinePayment().build()

        and:
        subject.save(op)

        and:
        def notification = new OnlinePaymentResultNotification()
        notification.setOnlinePaymentId(op.id)
        notification.setAmount(op.amount)
        notification.setCode(SUCCESS)
        notification.setCreatedAt(LocalDateTime.now())

        op.on(notification)

        and:
        subject.update(op)

        when:
        def after = subject.mustFindBy(op.id)

        then:
        assert after != null
        assert after.id == op.id
        assert after.version == op.version + 1
        assert after.status == OnlinePayment.Status.SUCCESS
        assert after.lastModifiedAt == notification.createdAt
    }

}
