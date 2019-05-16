package com.thebund1st.tiantong.web.rest.resources;

import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;
import lombok.Data;

@Data
public class OnlinePaymentResource {

    private String identifier;
    private double amount;
    private String method;
    private String status;
    private ProviderSpecificLaunchOnlinePaymentRequest providerSpecificRequest;


}
