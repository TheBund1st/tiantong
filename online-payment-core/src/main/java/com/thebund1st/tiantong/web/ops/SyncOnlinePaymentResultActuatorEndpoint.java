package com.thebund1st.tiantong.web.ops;

import com.thebund1st.tiantong.application.scheduling.SyncOnlinePaymentResultsCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

//FIXME how to test
@RequiredArgsConstructor
@Endpoint(id = "sync-online-payment-result")
public class SyncOnlinePaymentResultActuatorEndpoint {

    private final SyncOnlinePaymentResultsCommandHandler syncOnlinePaymentResultsCommandHandler;

    @WriteOperation
    public void sync() {
        //TODO return how many results required sync
        syncOnlinePaymentResultsCommandHandler.handle();
    }
}
