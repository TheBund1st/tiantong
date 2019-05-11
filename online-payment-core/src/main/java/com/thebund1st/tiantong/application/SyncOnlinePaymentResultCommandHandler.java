package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResultGateway;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class SyncOnlinePaymentResultCommandHandler {

    private final OnlinePaymentRepository onlinePaymentRepository;

    private final OnlinePaymentResultGateway onlinePaymentResultGateway;

    private final NotifyPaymentResultCommandHandler notifyPaymentResultCommandHandler;

    public Optional<OnlinePaymentResultNotification> handle(SyncOnlinePaymentResultCommand command) {
        OnlinePayment onlinePayment = onlinePaymentRepository
                .mustFindBy(OnlinePayment.Identifier.of(command.getOnlinePaymentId()));
        Optional<OnlinePaymentResultNotification> resultMaybe = onlinePaymentResultGateway.pull(onlinePayment);
        resultMaybe
                .ifPresent(notifyPaymentResultCommandHandler::handle);
        return resultMaybe;
    }

}
