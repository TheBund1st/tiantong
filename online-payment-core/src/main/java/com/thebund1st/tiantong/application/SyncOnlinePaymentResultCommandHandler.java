package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJob;
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobHandler;
import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SyncOnlinePaymentResultCommandHandler implements OnlinePaymentResultSynchronizationJobHandler {

    private final OnlinePaymentRepository onlinePaymentRepository;

    private final OnlinePaymentResultGateway onlinePaymentResultGateway;

    private final NotifyPaymentResultCommandHandler notifyPaymentResultCommandHandler;

    private final CloseOnlinePaymentCommandHandler closeOnlinePaymentCommandHandler;

    public Optional<OnlinePaymentResultNotification> handle(SyncOnlinePaymentResultCommand command) {
        OnlinePayment onlinePayment = onlinePaymentRepository
                .mustFindBy(OnlinePayment.Identifier.of(command.getOnlinePaymentId()));
        if (onlinePayment.isPending()) {
            Optional<OnlinePaymentResultNotification> resultMaybe = onlinePaymentResultGateway.pull(onlinePayment);
            if (resultMaybe.isPresent()) {
                notifyPaymentResultCommandHandler.handle(resultMaybe.get());
            } else {
                closeOnlinePaymentCommandHandler.closeIfNecessary(onlinePayment);
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
