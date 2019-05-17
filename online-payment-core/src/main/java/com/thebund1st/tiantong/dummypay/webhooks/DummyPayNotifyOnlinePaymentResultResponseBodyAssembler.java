package com.thebund1st.tiantong.dummypay.webhooks;

import com.thebund1st.tiantong.web.webhooks.NotifyOnlinePaymentResultResponseBodyAssembler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DummyPayNotifyOnlinePaymentResultResponseBodyAssembler implements NotifyOnlinePaymentResultResponseBodyAssembler {
    private static final String RESPONSE_BODY = "{\"result\":\"OK\"}";

    @Override
    public String toResponseBody() {
        return RESPONSE_BODY;
    }
}
