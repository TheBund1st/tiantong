package com.thebund1st.tiantong.web.rest;

import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand;
import com.thebund1st.tiantong.commands.SyncOnlinePaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import com.thebund1st.tiantong.web.rest.resources.OnlinePaymentResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class OnlinePaymentRestController {

    private final RequestOnlinePaymentCommandHandler onlinePaymentCommandHandler;
    private final OnlinePaymentProviderGateway onlinePaymentProviderGateway;

    //TODO make the url path configurable
    @PostMapping("/api/online/payments")
    public OnlinePaymentResource handle(@Valid @RequestBody RequestOnlinePaymentCommand command) {
        OnlinePayment onlinePayment = onlinePaymentCommandHandler.handle(command);
        ProviderSpecificRequest providerSpecificRequest = onlinePaymentProviderGateway.request(onlinePayment,
                command.getProviderSpecificRequest());
        return assemble(onlinePayment, providerSpecificRequest);
    }

    //TODO make the url path configurable
    @PostMapping("/api/online/payments/{onlinePaymentId}/resultSynchronizations")
    public OnlinePaymentResource handle(@PathVariable("onlinePaymentId") String onlinePaymentId) {
        OnlinePayment onlinePayment = onlinePaymentCommandHandler
                .handle(new SyncOnlinePaymentResultCommand(onlinePaymentId));
        return assemble(onlinePayment, null);
    }


    private OnlinePaymentResource assemble(OnlinePayment onlinePayment,
                                           ProviderSpecificRequest providerSpecificRequest) {
        OnlinePaymentResource resource = new OnlinePaymentResource();
        resource.setIdentifier(onlinePayment.getId().getValue());
        resource.setAmount(onlinePayment.getAmount());
        resource.setMethod(onlinePayment.getMethod().getValue());
        resource.setProviderSpecificRequest(providerSpecificRequest);
        return resource;
    }
}
