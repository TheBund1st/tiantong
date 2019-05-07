package com.thebund1st.tiantong.commands

import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand

class RequestOnlinePaymentCommandValidatorTest extends Specification {

    private Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    @Unroll("The validation is #pass given the amount is #amount")
    def "it should fail the validation given the amount is negative"(double amount, boolean pass) {
        expect:
        RequestOnlinePaymentCommand command = aRequestOnlinePaymentCommand().amountIs(amount).build()
        def constraintViolations = validator.validate(command)
        assert constraintViolations.isEmpty() == pass

        where:
        amount | pass
        1.0    | true
        0      | false
        -1     | false
    }
}
