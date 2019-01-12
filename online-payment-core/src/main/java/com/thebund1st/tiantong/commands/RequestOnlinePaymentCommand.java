package com.thebund1st.tiantong.commands;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.thebund1st.tiantong.json.ProviderSpecificInfoDeserializer;
import com.thebund1st.tiantong.core.OnlinePayment.Correlation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RequestOnlinePaymentCommand {
    //FIXME introduce MonetaryAmount
    private double amount;
    //FIXME introduce Method
    private String method;

    private Correlation correlation;

    private String subject;

    private String body;

    //WeChat Pay Specific
    //TODO how to deal with these provider specific fields
    private String openId;
    //TODO how to deal with these provider specific fields
    private String productId;

    @JsonDeserialize(using = ProviderSpecificInfoDeserializer.class)
    private String providerSpecificInfo;
}
