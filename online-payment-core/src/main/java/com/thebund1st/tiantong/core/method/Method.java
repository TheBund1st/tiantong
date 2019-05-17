package com.thebund1st.tiantong.core.method;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Method {
    private String value;

    private Method(String value) {
        this.value = value;
    }

    public static Method of(String value) {
        return new Method(value);
    }
}
