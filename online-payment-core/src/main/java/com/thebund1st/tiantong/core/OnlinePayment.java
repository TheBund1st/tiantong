package com.thebund1st.tiantong.core;

import com.thebund1st.tiantong.events.EventIdentifier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

import static com.thebund1st.tiantong.core.OnlinePayment.Status.PENDING;
import static com.thebund1st.tiantong.core.OnlinePayment.Status.SUCCESS;

@EqualsAndHashCode()
@ToString()
@Getter
@Setter
public class OnlinePayment {

    private Identifier id;
    //FIXME: introduce monetary amount
    private double amount;
    private Status status = PENDING;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;
    private EventIdentifier notifiedBy;

    public OnlinePayment(Identifier id, ZonedDateTime time) {
        this.id = id;
        this.createdAt = time;
        this.lastModifiedAt = time;
    }

    public void succeed(double amount, EventIdentifier eventId, ZonedDateTime now) {
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
        PENDING(0), SUCCESS(1);

        private int value;

        Status(int value) {
            this.value = value;
        }
    }
}
