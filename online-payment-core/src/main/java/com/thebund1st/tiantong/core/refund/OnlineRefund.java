package com.thebund1st.tiantong.core.refund;

import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode(of = "id")
@ToString()
@Getter
@Setter
public class OnlineRefund {

    private Identifier id;
    private int version = 1;
    //FIXME: introduce monetary amount
    private OnlinePayment.Identifier onlinePaymentId;
    private double onlinePaymentAmount;
    private double amount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;


    public OnlineRefund() {

    }

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

    public enum Status {
        UNKNOWN(-1), PENDING(0), SUCCESS(1), FAILURE(2);

        @Getter
        private int value;

        Status(int value) {
            this.value = value;
        }

        public static Status of(int value) {
            for (Status status : values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            return UNKNOWN;
        }
    }
}