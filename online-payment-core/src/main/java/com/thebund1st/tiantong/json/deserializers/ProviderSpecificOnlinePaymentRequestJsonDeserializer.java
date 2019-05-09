package com.thebund1st.tiantong.json.deserializers;

import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;

public interface ProviderSpecificOnlinePaymentRequestJsonDeserializer {

    ProviderSpecificOnlinePaymentRequest deserialize(String json);

}
