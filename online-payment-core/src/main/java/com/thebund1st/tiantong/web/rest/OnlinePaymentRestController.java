package com.thebund1st.tiantong.web.rest;

import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.application.RequestOnlineRefundCommandHandler;
import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand;
import com.thebund1st.tiantong.commands.RequestOnlineRefundCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentProviderGateway;
import com.thebund1st.tiantong.core.OnlineRefundProviderGateway;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import com.thebund1st.tiantong.web.rest.resources.OnlinePaymentResource;
import com.thebund1st.tiantong.web.rest.resources.OnlineRefundResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OnlinePaymentRestController {

    private final RequestOnlinePaymentCommandHandler onlinePaymentCommandHandler;
    private final OnlinePaymentProviderGateway onlinePaymentProviderGateway;
    private final RequestOnlineRefundCommandHandler onlineRefundCommandHandler;
    private final OnlineRefundProviderGateway onlineRefundProviderGateway;


    //TODO make the url path configurable
    @PostMapping("/api/online/payments")
    public OnlinePaymentResource handle(@RequestBody RequestOnlinePaymentCommand command) {
        OnlinePayment onlinePayment = onlinePaymentCommandHandler.handle(command);
        ProviderSpecificRequest providerSpecificRequest = onlinePaymentProviderGateway.request(onlinePayment);
        return assemble(onlinePayment, providerSpecificRequest);
    }

    @PostMapping("/api/online/payments/{onlinePaymentId}/refunds")
    public OnlineRefundResource handle(@PathVariable("onlinePaymentId") String onlinePaymentId) {
        RequestOnlineRefundCommand command = new RequestOnlineRefundCommand();
        command.setOnlinePaymentId(onlinePaymentId);
        OnlineRefund refund = onlineRefundCommandHandler.handle(command);
        onlineRefundProviderGateway.request(refund);
        return assemble(refund);
    }

    private OnlineRefundResource assemble(OnlineRefund refund) {
        OnlineRefundResource resource = new OnlineRefundResource();
        resource.setIdentifier(refund.getId().getValue());
        return resource;
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
