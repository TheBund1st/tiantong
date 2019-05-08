package com.thebund1st.tiantong.json.deserializers;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.thebund1st.tiantong.core.MethodAware;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;

public abstract class AbstractMethodBasedProviderSpecificOnlinePaymentRequestDeserializer
        extends JsonDeserializer<ProviderSpecificOnlinePaymentRequest>
        implements MethodAware {

}
