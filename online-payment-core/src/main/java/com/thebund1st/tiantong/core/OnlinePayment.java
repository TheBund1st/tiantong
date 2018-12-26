package com.thebund1st.tiantong.core;

import com.thebund1st.tiantong.core.exceptions.FakeOnlinePaymentNotificationException;
import com.thebund1st.tiantong.core.exceptions.OnlinePaymentAlreadyClosedException;
import com.thebund1st.tiantong.events.EventIdentifier;
import com.thebund1st.tiantong.events.OnlinePaymentFailureNotificationReceivedEvent;
import com.thebund1st.tiantong.events.OnlinePaymentSuccessNotificationReceivedEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.thebund1st.tiantong.core.OnlinePayment.Status.FAILURE;
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

    private String subject;

    // WeChat Pay specific
    private String openId;
    private String productId;
    private String rawNotification;

    public OnlinePayment() {

    }

    public OnlinePayment(Identifier id,
                         LocalDateTime time) {
        this.id = id;
        this.createdAt = time;
        this.lastModifiedAt = time;
    }

    public void on(OnlinePaymentSuccessNotificationReceivedEvent event, LocalDateTime now) {
        if (isClosed()) {
            throw new OnlinePaymentAlreadyClosedException(getId(), getStatus(), event);
        }
        if (amountMismatches(event.getAmount())) {
            throw new FakeOnlinePaymentNotificationException(getId(), getAmount(), event);
        }
        this.status = SUCCESS;
        this.rawNotification = event.getRaw();
        this.notifiedBy = event.getEventId();
        this.lastModifiedAt = now;
    }

    private boolean amountMismatches(double amount) {
        return getAmount() != amount;
    }

    private boolean isClosed() {
        return getStatus() != PENDING;
    }

    public void on(OnlinePaymentFailureNotificationReceivedEvent event, LocalDateTime now) {
        if (isClosed()) {
            throw new OnlinePaymentAlreadyClosedException(getId(), getStatus(), event);
        }
        if (amountMismatches(event.getAmount())) {
            throw new FakeOnlinePaymentNotificationException(getId(), getAmount(), event);
        }
        this.status = FAILURE;
        this.rawNotification = event.getRaw();
        this.notifiedBy = event.getEventId();
        this.lastModifiedAt = now;
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
