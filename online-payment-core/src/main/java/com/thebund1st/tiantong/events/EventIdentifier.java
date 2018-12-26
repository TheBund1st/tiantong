package com.thebund1st.tiantong.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class EventIdentifier {
    private String value;

    private EventIdentifier(String value) {
        this.value = value;
    }

    public static EventIdentifier of(String value) {
        return new EventIdentifier(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
