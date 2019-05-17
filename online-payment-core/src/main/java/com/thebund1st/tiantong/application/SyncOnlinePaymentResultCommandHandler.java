package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJob;
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobHandler;
import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SyncOnlinePaymentResultCommandHandler implements OnlinePaymentResultSynchronizationJobHandler {

    private final OnlinePaymentRepository onlinePaymentRepository;

    private final OnlinePaymentResultGateway onlinePaymentResultGateway;

    private final NotifyOnlinePaymentResultCommandHandler notifyOnlinePaymentResultCommandHandler;

    private final ProviderSpecificCloseOnlinePaymentGateway providerSpecificCloseOnlinePaymentGateway;

    private final Clock clock;

    public Optional<OnlinePaymentResultNotification> handle(SyncOnlinePaymentResultCommand command) {
        OnlinePayment onlinePayment = onlinePaymentRepository
                .mustFindBy(OnlinePayment.Identifier.of(command.getOnlinePaymentId()));
        if (onlinePayment.isPending()) {
            Optional<OnlinePaymentResultNotification> resultMaybe = onlinePaymentResultGateway.pull(onlinePayment);
            resultMaybe.ifPresent(notifyOnlinePaymentResultCommandHandler::handle);
            if (OnlinePayment.shouldCloseSpecification(clock.now()).test(onlinePayment)) {
                providerSpecificCloseOnlinePaymentGateway.close(onlinePayment);
            }
            return resultMaybe;
        } else {
            return Optional.empty();
        }

    }

    @Override
    public void handle(OnlinePaymentResultSynchronizationJob command) {
        handle(new SyncOnlinePaymentResultCommand(command.getOnlinePaymentId().getValue()));
    }
}
