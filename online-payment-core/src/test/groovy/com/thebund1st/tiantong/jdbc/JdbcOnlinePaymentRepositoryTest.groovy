package com.thebund1st.tiantong.jdbc

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

import java.time.LocalDateTime

import static com.github.hippoom.tdb.GenericTestDataListBuilder.listOfSize
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
        assert actual.expiresAt == op.expiresAt
        assert actual.subject == op.subject
        assert actual.body == op.body
        assert actual.providerSpecificOnlinePaymentRequest == op.providerSpecificOnlinePaymentRequest
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

    def "it should find online payments that requires result synchronizations"() {
        given:
        def now = LocalDateTime.now()
        def _3_minutes_ago = now.minusMinutes(3)

        and:
        def before = subject.find(_3_minutes_ago, PageRequest.of(0, 2))

        and:
        def onlinePayments = listOfSize(4, { anOnlinePayment() })
                .theFirst(3, { it.createdAt(_3_minutes_ago) })
                .theLast(1, { it.createdAt(now) })
                .build { it.build() }
        onlinePayments.forEach { subject.save(it) }

        when:
        def after = subject.find(_3_minutes_ago, PageRequest.of(0, 2))

        then:
        assert after.totalElements == before.totalElements + 3
    }

}
