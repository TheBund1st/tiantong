package com.thebund1st.tiantong.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode()
@ToString()
@Getter
@Setter
public class OnlinePaymentResponse {

    private Identifier id;
    private OnlinePayment.Identifier onlinePaymentId;
    private double amount;
    private String text;
    private Code code;
    private LocalDateTime createdAt;


    @Getter
    @EqualsAndHashCode
    public static class Identifier {
        private String value;

        private Identifier(String value) {
            this.value = value;
        }

        public static Identifier of(String value) {
            return new Identifier(value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Code {
        UNKNOWN(-1), SUCCESS(1), FAILURE(2);

        @Getter
        private int value;

        Code(int value) {
            this.value = value;
        }

        public static Code of(int value) {
            for (Code status : values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            return UNKNOWN;
        }
    }
}
