package com.thebund1st.tiantong.web.rest;

import com.thebund1st.tiantong.application.RequestOnlineRefundCommandHandler;
import com.thebund1st.tiantong.commands.RequestOnlineRefundCommand;
import com.thebund1st.tiantong.core.OnlineRefundProviderGateway;
import com.thebund1st.tiantong.core.refund.OnlineRefund;
import com.thebund1st.tiantong.web.rest.resources.OnlineRefundResource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(prefix = "tiantong.refund", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "#{requestMappingProperties.prefix}")
public class OnlineRefundRestController {

    private final RequestOnlineRefundCommandHandler onlineRefundCommandHandler;
    private final OnlineRefundProviderGateway onlineRefundProviderGateway;

    @PostMapping("/online/payments/{onlinePaymentId}/refunds")
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

}
