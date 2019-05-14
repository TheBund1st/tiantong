package com.thebund1st.tiantong.web.rest;

import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.application.SyncOnlinePaymentResultCommandHandler;
import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand;
import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.OnlinePaymentRepository;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.core.ProviderSpecificUserAgentOnlinePaymentRequest;
import com.thebund1st.tiantong.web.rest.resources.OnlinePaymentResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.PAYMENT_REQUIRED;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "#{requestMappingProperties.prefix}")
public class OnlinePaymentRestController {

    private final RequestOnlinePaymentCommandHandler onlinePaymentCommandHandler;
    private final OnlinePaymentProviderGateway onlinePaymentProviderGateway;
    private final SyncOnlinePaymentResultCommandHandler syncOnlinePaymentResultCommandHandler;
    private final OnlinePaymentRepository onlinePaymentRepository;

    @PostMapping("/online/payments")
    public OnlinePaymentResource handle(@Valid @RequestBody RequestOnlinePaymentCommand command) {
        OnlinePayment onlinePayment = onlinePaymentCommandHandler.handle(command);
        ProviderSpecificUserAgentOnlinePaymentRequest providerSpecificRequest = onlinePaymentProviderGateway.request(onlinePayment,
                command.getProviderSpecificRequest());
        return assemble(onlinePayment, providerSpecificRequest);
    }

    @GetMapping("/online/payments/{onlinePaymentId}")
    public OnlinePaymentResource findBy(@PathVariable("onlinePaymentId") String onlinePaymentId) {
        OnlinePayment onlinePayment = onlinePaymentRepository.mustFindBy(OnlinePayment.Identifier.of(onlinePaymentId));
        return assemble(onlinePayment);
    }

    @PostMapping("/online/payments/{onlinePaymentId}/resultSynchronizations")
    public ResponseEntity handle(@PathVariable("onlinePaymentId") String onlinePaymentId) {
        Optional<OnlinePaymentResultNotification> resultMaybe = syncOnlinePaymentResultCommandHandler
                .handle(new SyncOnlinePaymentResultCommand(onlinePaymentId));
        return resultMaybe
                .map(r -> new ResponseEntity(CREATED))
                .orElse(new ResponseEntity(PAYMENT_REQUIRED));
    }


    private OnlinePaymentResource assemble(OnlinePayment onlinePayment,
                                           ProviderSpecificUserAgentOnlinePaymentRequest providerSpecificRequest) {
        OnlinePaymentResource resource = assemble(onlinePayment);
        resource.setProviderSpecificRequest(providerSpecificRequest);
        return resource;
    }

    private OnlinePaymentResource assemble(OnlinePayment onlinePayment) {
        OnlinePaymentResource resource = new OnlinePaymentResource();
        resource.setIdentifier(onlinePayment.getId().getValue());
        resource.setAmount(onlinePayment.getAmount());
        resource.setMethod(onlinePayment.getMethod().getValue());
        resource.setStatus(onlinePayment.getStatus().name());
        return resource;
    }
}
