package com.thebund1st.tiantong.commands;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.thebund1st.tiantong.core.OnlinePayment.Correlation;
import com.thebund1st.tiantong.core.payable.Payable;
import com.thebund1st.tiantong.core.payment.FlattenedProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.json.deserializers.ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher;
import com.thebund1st.tiantong.json.deserializers.ProviderSpecificInfoJsonDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CreateOnlinePaymentCommand {
    //FIXME introduce MonetaryAmount
    @DecimalMin(value = "0", inclusive = false)
    private double amount;
    //FIXME introduce Method
    private String method;

    private Payable payable;

    private String subject;

    private String body;


    @Valid
    @NotNull
    @JsonDeserialize(using = ProviderSpecificCreateOnlinePaymentRequestJsonDeserializerDispatcher.class)
    private ProviderSpecificCreateOnlinePaymentRequest providerSpecificRequest = new FlattenedProviderSpecificCreateOnlinePaymentRequest();

    /***The following fields are deprecated and will be removed in future versions***/
    @Deprecated
    private Correlation correlation;

    @Deprecated
    private String openId;
    @Deprecated
    private String productId;

    @Deprecated
    @JsonDeserialize(using = ProviderSpecificInfoJsonDeserializer.class)
    private String providerSpecificInfo;
}
