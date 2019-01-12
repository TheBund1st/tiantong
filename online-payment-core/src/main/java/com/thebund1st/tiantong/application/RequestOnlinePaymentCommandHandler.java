package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class RequestOnlinePaymentCommandHandler {

    private final OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator;
    private final OnlinePaymentRepository onlinePaymentRepository;
    private final Clock clock;

    public OnlinePayment handle(RequestOnlinePaymentCommand command) {
        OnlinePayment op = new OnlinePayment(onlinePaymentIdentifierGenerator.nextIdentifier(), clock.now());
        op.setAmount(command.getAmount());
        op.setMethod(OnlinePayment.Method.of(command.getMethod()));
        op.setCorrelation(command.getCorrelation());
        op.setSubject(command.getSubject());
        op.setBody(command.getBody());
        op.setProviderSpecificInfo(command.getProviderSpecificInfo());
        onlinePaymentRepository.save(op);
        return op;
    }
}
