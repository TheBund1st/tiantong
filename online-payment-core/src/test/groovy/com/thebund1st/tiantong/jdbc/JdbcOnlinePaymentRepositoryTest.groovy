package com.thebund1st.tiantong.jdbc

import com.thebund1st.tiantong.events.EventIdentifier
import com.thebund1st.tiantong.events.OnlinePaymentSuccessNotificationReceivedEvent
import com.thebund1st.tiantong.utils.Randoms
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

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
        assert actual.openId == op.openId
        assert actual.productId == op.productId
    }

    def "it should update op"() {
        given:
        def op = anOnlinePayment().build()

        and:
        subject.save(op)

        and:
        def event = new OnlinePaymentSuccessNotificationReceivedEvent(EventIdentifier.of(Randoms.randomStr()), op.getId(), op.amount)
        event.setRaw("<xml>this is a raw xml<xml>")

        when:
        op.on(event, LocalDateTime.now())

        and:
        subject.update(op)

        then:
        def after = subject.mustFindBy(op.id)

        assert after != null
        assert after.id == op.id
        assert after.version == op.version + 1
        assert after.status == op.status
        assert after.lastModifiedAt == op.lastModifiedAt
        assert after.rawNotification == event.raw
    }

}
