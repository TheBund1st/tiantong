package com.thebund1st.tiantong.jdbc


import com.thebund1st.tiantong.events.OnlinePaymentSuccessEvent
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePayment.Status.SUCCESS
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
        assert actual.body == op.body
        assert actual.providerSpecificInfo == op.providerSpecificInfo
    }

    def "it should update op"() {
        given:
        def op = anOnlinePayment().build()

        and:
        subject.save(op)

        and:
        def event = new OnlinePaymentSuccessEvent()
        event.setOnlinePaymentId(op.id)
        event.setOnlinePaymentVersion(op.version)
        event.setCorrelation(op.correlation)
        event.setWhen(LocalDateTime.now())
        event.setNotificationBody("<xml>This is a xml notification</xml>")

        and:
        subject.on(event)

        when:
        def after = subject.mustFindBy(op.id)

        then:
        assert after != null
        assert after.id == op.id
        assert after.version == op.version + 1
        assert after.status == SUCCESS
        assert after.lastModifiedAt == event.when
    }

}
