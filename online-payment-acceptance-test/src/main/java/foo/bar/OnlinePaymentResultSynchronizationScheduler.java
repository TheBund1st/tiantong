package foo.bar;

import com.thebund1st.tiantong.application.scheduling.SyncOnlinePaymentResultsCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OnlinePaymentResultSynchronizationScheduler {

    private final SyncOnlinePaymentResultsCommandHandler syncOnlinePaymentResultsCommandHandler;

    @Scheduled(cron = "* * * * * *") //every second
    public void schedule() {
        syncOnlinePaymentResultsCommandHandler.handle();
    }
}
