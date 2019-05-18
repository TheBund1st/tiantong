package com.thebund1st.tiantong.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.payable.Payable;
import com.thebund1st.tiantong.json.deserializers.OnlinePaymentIdJsonDeserializer;
import com.thebund1st.tiantong.json.serializers.OnlinePaymentIdJsonSerializer;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class OnlinePaymentSucceededEvent {
    @JsonDeserialize(using = OnlinePaymentIdJsonDeserializer.class)
    @JsonSerialize(using = OnlinePaymentIdJsonSerializer.class)
    private OnlinePayment.Identifier onlinePaymentId;
    private int onlinePaymentVersion;
    @Deprecated
    private OnlinePayment.Correlation correlation;
    private Payable payable;
    private double amount;
    private LocalDateTime when;
}
