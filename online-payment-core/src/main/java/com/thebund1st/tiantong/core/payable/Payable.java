package com.thebund1st.tiantong.core.payable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
public class Payable {
    private String context;
    private String objectId;

    public static Payable of(String context, String objectId) {
        return new Payable(context, objectId);
    }
}
