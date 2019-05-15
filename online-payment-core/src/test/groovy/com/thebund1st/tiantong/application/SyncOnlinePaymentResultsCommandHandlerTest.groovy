package com.thebund1st.tiantong.application

import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJob
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobHandler
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobStore
import com.thebund1st.tiantong.application.scheduling.SyncOnlinePaymentResultsCommandHandler
import com.thebund1st.tiantong.time.Clock
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDateTime

import static com.github.hippoom.tdb.GenericTestDataListBuilder.listOfSize
import static com.thebund1st.tiantong.application.job.OnlinePaymentResultSynchronizationJobFixture.anOnlinePaymentResultSynchronizationJob
import static com.thebund1st.tiantong.core.OnlinePaymentFixture.anOnlinePayment

class SyncOnlinePaymentResultsCommandHandlerTest extends Specification {

    private OnlinePaymentResultSynchronizationJobStore onlinePaymentResultSynchronizationJobStore = Mock()
    private OnlinePaymentResultSynchronizationJobHandler onlinePaymentResultSynchronizationJobHandler = Mock()
    private Clock clock = Mock()
    private SyncOnlinePaymentResultsCommandHandler subject

    def setup() {
        subject = new SyncOnlinePaymentResultsCommandHandler(
                onlinePaymentResultSynchronizationJobStore,
                onlinePaymentResultSynchronizationJobHandler,
                clock)
        subject.setBatchSize(2)
    }

    def "it should find filter online payments that is in turn to result sync"() {
        given:
        def now = LocalDateTime.now()
        clock.now() >> now
        def createdAt  = now.minusMinutes(3)

        and:
        def op_1 = anOnlinePayment().build()
        def op_2 = anOnlinePayment().build()
        def op_3 = anOnlinePayment().build()

        def jobsOnPage1 = listOfSize(2, { anOnlinePaymentResultSynchronizationJob() })
                .number(1, { it.with(op_1) })
                .number(2, { it.with(op_2) })
                .build({ it.build() })

        def jobsOnPage2 = listOfSize(1, { anOnlinePaymentResultSynchronizationJob() })
                .number(1, { it.with(op_3) })
                .build({ it.build() })

        onlinePaymentResultSynchronizationJobStore.find(createdAt, PageRequest.of(0, 2)) >>
                new PageImpl<OnlinePaymentResultSynchronizationJob>(jobsOnPage1, PageRequest.of(0, 2), 3)

        onlinePaymentResultSynchronizationJobStore.find(createdAt, PageRequest.of(1, 2)) >>
                new PageImpl<OnlinePaymentResultSynchronizationJob>(jobsOnPage2, PageRequest.of(1, 2), 3)


        when:
        subject.handle()

        then:

        with(onlinePaymentResultSynchronizationJobHandler) {
            1 * handle(jobsOnPage1.get(0))
            1 * handle(jobsOnPage1.get(1))
            1 * handle(jobsOnPage2.get(0))
        }

    }
}
