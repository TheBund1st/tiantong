package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.MakeOnlinePaymentCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.events.OnlinePaymentSuccessNotificationReceivedEvent;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class OnlinePaymentCommandHandler {

    private final OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator;
    private final OnlinePaymentRepository onlinePaymentRepository;
    private final Clock clock;

    public OnlinePayment handle(MakeOnlinePaymentCommand command) {
        OnlinePayment op = new OnlinePayment(onlinePaymentIdentifierGenerator.nextIdentifier(), clock.now());
        op.setAmount(command.getAmount());
        op.setMethod(OnlinePayment.Method.of(command.getMethod()));
        op.setCorrelation(command.getCorrelation());
        op.setOpenId(command.getOpenId());
        op.setProductId(command.getProductId());
        onlinePaymentRepository.save(op);
        return op;
    }

    public void on(OnlinePaymentSuccessNotificationReceivedEvent event) {
        OnlinePayment op = onlinePaymentRepository.mustFindBy(event.getOnlinePaymentId());
        op.succeed(event.getAmount(), event.getEventId(), clock.now());
    }
}
