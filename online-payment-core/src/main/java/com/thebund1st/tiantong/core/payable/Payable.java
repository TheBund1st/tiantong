package com.thebund1st.tiantong.core.payable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

/**
 * The object that requires payment.
 *
 * @see <a href="https://github.com/TheBund1st/tiantong/wiki/%E5%BE%85%E6%94%AF%E4%BB%98%E7%89%A9">wiki</a>
 * @since {{ RELEASE }}
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
public class Payable {
    private String context;
    private String objectId;

    public static Payable of(String context, String objectId) {
        return new Payable(context, objectId);
    }
}
