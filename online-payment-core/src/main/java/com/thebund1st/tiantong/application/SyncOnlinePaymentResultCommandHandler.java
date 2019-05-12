package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJob;
import com.thebund1st.tiantong.application.scheduling.OnlinePaymentResultSynchronizationJobHandler;
import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class SyncOnlinePaymentResultCommandHandler implements OnlinePaymentResultSynchronizationJobHandler {

    private final OnlinePaymentRepository onlinePaymentRepository;

    private final OnlinePaymentResultGateway onlinePaymentResultGateway;

    private final NotifyPaymentResultCommandHandler notifyPaymentResultCommandHandler;

    public Optional<OnlinePaymentResultNotification> handle(SyncOnlinePaymentResultCommand command) {
        OnlinePayment onlinePayment = onlinePaymentRepository
                .mustFindBy(OnlinePayment.Identifier.of(command.getOnlinePaymentId()));
        //FIXME filter payments that is not pending
        Optional<OnlinePaymentResultNotification> resultMaybe = onlinePaymentResultGateway.pull(onlinePayment);
        resultMaybe
                .ifPresent(notifyPaymentResultCommandHandler::handle);
        return resultMaybe;
    }

    @Override
    public void handle(OnlinePaymentResultSynchronizationJob command) {
        handle(new SyncOnlinePaymentResultCommand(command.getOnlinePaymentId().getValue()));
    }
}
