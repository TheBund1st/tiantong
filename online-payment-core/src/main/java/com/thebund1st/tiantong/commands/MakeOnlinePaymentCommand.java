package com.thebund1st.tiantong.commands;

import com.thebund1st.tiantong.core.OnlinePayment.Correlation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MakeOnlinePaymentCommand {
    //FIXME introduce MonetaryAmount
    private double amount;
    //FIXME introduce Method
    private String method;

    private Correlation correlation;

    //WeChat Pay Specific
    //TODO how to deal with these provider specific fields
    private String openId;
    //TODO how to deal with these provider specific fields
    private String productId;
}
