package com.thebund1st.tiantong.core;

import com.thebund1st.tiantong.events.EventIdentifier;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.thebund1st.tiantong.core.OnlinePayment.Status.PENDING;
import static com.thebund1st.tiantong.core.OnlinePayment.Status.SUCCESS;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode()
@ToString()
@Getter
@Setter
public class OnlinePayment {

    private Identifier id;
    private int version = 1;
    //FIXME: introduce monetary amount
    private double amount;
    private Status status = PENDING;
    private Correlation correlation;
    private Method method;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private EventIdentifier notifiedBy;

    // WeChat Pay specific
    private String openId;
    private String productId;

    public OnlinePayment() {

    }

    public OnlinePayment(Identifier id,
                         LocalDateTime time) {
        this.id = id;
        this.createdAt = time;
        this.lastModifiedAt = time;
    }

    public void succeed(double amount, EventIdentifier eventId, LocalDateTime now) {
        //TODO amount check
        //TODO status check
        this.status = SUCCESS;
        this.notifiedBy = eventId;
        this.lastModifiedAt = now;
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Identifier {
        private String value;

        private Identifier(String value) {
            this.value = value;
        }

        public static Identifier of(String value) {
            return new Identifier(value);
        }
    }

    public enum Status {
        UNKNOWN(-1), PENDING(0), SUCCESS(1);

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

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Method {
        private String value;

        private Method(String value) {
            this.value = value;
        }

        public static Method of(String value) {
            return new Method(value);
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor(access = PRIVATE)
    public static class Correlation {
        private String key;
        private String value;

        private Correlation(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static Correlation of(String key, String value) {
            return new Correlation(key, value);
        }
    }
}
