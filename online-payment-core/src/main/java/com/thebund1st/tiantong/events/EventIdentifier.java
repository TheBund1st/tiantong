package com.thebund1st.tiantong.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class EventIdentifier {
    private String value;

    private EventIdentifier(String value) {
        this.value = value;
    }

    public static EventIdentifier of(String value) {
        return new EventIdentifier(value);
    }
}
