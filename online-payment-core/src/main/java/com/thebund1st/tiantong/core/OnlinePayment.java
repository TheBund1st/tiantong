package com.thebund1st.tiantong.core;

import com.thebund1st.tiantong.core.exceptions.FakeOnlinePaymentNotificationException;
import com.thebund1st.tiantong.core.exceptions.OnlinePaymentAlreadyClosedException;
import com.thebund1st.tiantong.core.method.Method;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.core.payment.FlattenedProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.events.OnlinePaymentClosedEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static com.thebund1st.tiantong.core.OnlinePayment.Status.CLOSED;
import static com.thebund1st.tiantong.core.OnlinePayment.Status.FAILURE;
import static com.thebund1st.tiantong.core.OnlinePayment.Status.PENDING;
import static com.thebund1st.tiantong.core.OnlinePayment.Status.SUCCESS;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode(of = "id")
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
    private LocalDateTime expiresAt;

    private String subject;
    private String body;
    private String providerSpecificInfo;
    private ProviderSpecificCreateOnlinePaymentRequest providerSpecificOnlinePaymentRequest = new FlattenedProviderSpecificCreateOnlinePaymentRequest();


    public OnlinePayment() {

    }

    public OnlinePayment(Identifier id,
                         LocalDateTime time) {
        this.id = id;
        this.createdAt = time;
        this.lastModifiedAt = time;
    }

    public void on(OnlinePaymentResultNotification notification) {
        if (isClosed()) {
            throw new OnlinePaymentAlreadyClosedException(this, notification);
        }
        if (notification.getCode() == OnlinePaymentResultNotification.Code.SUCCESS) {
            if (amountMismatches(notification.getAmount())) {
                throw new FakeOnlinePaymentNotificationException(this, notification);
            }
        }
        this.status = mapped(notification.getCode());
        this.lastModifiedAt = notification.getCreatedAt();
    }

    private Status mapped(OnlinePaymentResultNotification.Code code) {
        if (OnlinePaymentResultNotification.Code.SUCCESS == code) {
            return SUCCESS;
        } else if (OnlinePaymentResultNotification.Code.FAILURE == code) {
            return FAILURE;
        } else if (OnlinePaymentResultNotification.Code.CLOSED == code) {
            return CLOSED;
        } else {
            throw new IllegalStateException(String.format("Cannot map %s to online payment status", code));
        }
    }

    private boolean amountMismatches(double amount) {
        return getAmount() != amount;
    }

    private boolean isClosed() {
        return !isPending();
    }

    public boolean isPending() {
        return PENDING == getStatus();
    }

    public static Predicate<OnlinePayment> shouldCloseSpecification(LocalDateTime now) {
        Predicate<OnlinePayment> isPending = OnlinePayment::isPending;
        Predicate<OnlinePayment> ago = (onlinePayment) -> onlinePayment.expiresBy(now);
        return isPending.and(ago);
    }

    private boolean expiresBy(LocalDateTime now) {
        return getExpiresAt().isBefore(now);
    }

    public OnlinePaymentClosedEvent toClosedEvent(LocalDateTime now) {
        OnlinePaymentClosedEvent event = new OnlinePaymentClosedEvent();
        event.setOnlinePaymentId(getId());
        event.setOnlinePaymentVersion(getVersion());
        event.setCorrelation(correlation);
        event.setWhen(now);
        return event;
    }

    public void close(LocalDateTime now) {
        if (isPending()) {
            this.status = CLOSED;
            this.lastModifiedAt = now;
        } else {
            throw new OnlinePaymentAlreadyClosedException(this);
        }
    }

    public void expires(Duration expires) {
        this.expiresAt = getCreatedAt().plusSeconds(expires.getSeconds());
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
        UNKNOWN(-1), PENDING(0), SUCCESS(1), FAILURE(2), CLOSED(3);

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
