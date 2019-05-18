package com.thebund1st.tiantong.core.payee;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

/**
 * The party that receives payment.
 *
 * @see <a href="https://github.com/TheBund1st/tiantong/wiki/%E6%94%B6%E6%AC%BE%E6%96%B9">wiki</a>
 * @since {{ RELEASE }}
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
public class Payee {
    public static final Payee UNSPECIFIED = Payee.of(null, null);
    private String context;
    private String objectId;

    public static Payee of(String context, String objectId) {
        return new Payee(context, objectId);
    }

    public static Payee unspecified() {
        return UNSPECIFIED;
    }
}
