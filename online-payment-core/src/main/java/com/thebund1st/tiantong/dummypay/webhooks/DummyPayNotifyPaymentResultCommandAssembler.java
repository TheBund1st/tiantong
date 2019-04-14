package com.thebund1st.tiantong.dummypay.webhooks;

import com.jayway.jsonpath.JsonPath;
import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.web.webhooks.NotifyPaymentResultCommandAssembler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class DummyPayNotifyPaymentResultCommandAssembler implements NotifyPaymentResultCommandAssembler {
    private static final String SUCCESS = "SUCCESS";

    @SneakyThrows
    @Override
    public NotifyPaymentResultCommand from(String rawNotification) {
        String onlinePaymentId = JsonPath.read(rawNotification, "$.onlinePaymentId");
        double amount = JsonPath.read(rawNotification, "$.amount");
        String result = JsonPath.read(rawNotification, "$.result");
        return new NotifyPaymentResultCommand(
                OnlinePayment.Identifier.of(onlinePaymentId),
                amount,
                SUCCESS.equals(result),
                rawNotification);
    }
}
