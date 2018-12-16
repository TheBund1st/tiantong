package com.thebund1st.tiantong.web.rest;

import com.thebund1st.tiantong.application.OnlinePaymentCommandHandler;
import com.thebund1st.tiantong.commands.MakeOnlinePaymentCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
//TODO make the url path configurable
public class OnlinePaymentRestController {

    private final OnlinePaymentCommandHandler onlinePaymentCommandHandler;

    @PostMapping("/api/online/payments")
    public void handle(@RequestBody MakeOnlinePaymentCommand command) {
        onlinePaymentCommandHandler.handle(command);
    }
}
