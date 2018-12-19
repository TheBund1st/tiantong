package com.thebund1st.tiantong.core;

import java.util.UUID;

public class OnlinePaymentRepository {
    public void save(OnlinePayment model) {

    }

    public OnlinePayment.Identifier nextIdentifier() {
        return OnlinePayment.Identifier.of(UUID.randomUUID().toString().replace("-", ""));
    }


    public OnlinePayment mustFindBy(OnlinePayment.Identifier id) {
        return null;
    }
}
