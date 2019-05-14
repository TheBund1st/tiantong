package com.thebund1st.tiantong.application;

import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentIdentifierGenerator;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Validated
public class RequestOnlinePaymentCommandHandler {

    private final OnlinePaymentIdentifierGenerator onlinePaymentIdentifierGenerator;
    private final OnlinePaymentRepository onlinePaymentRepository;
    private final Clock clock;
    @Setter
    private Duration expires = Duration.ofMinutes(30);

    public OnlinePayment handle(@Valid RequestOnlinePaymentCommand command) {
        OnlinePayment op = new OnlinePayment(onlinePaymentIdentifierGenerator.nextIdentifier(), clock.now());
        op.setAmount(command.getAmount());
        op.setMethod(OnlinePayment.Method.of(command.getMethod()));
        op.setCorrelation(command.getCorrelation());
        op.setSubject(command.getSubject());
        op.setBody(command.getBody());
        op.setProviderSpecificInfo(command.getProviderSpecificInfo());
        op.setProviderSpecificOnlinePaymentRequest(command.getProviderSpecificRequest());
        op.expires(expires);
        onlinePaymentRepository.save(op);
        return op;
    }

}
