package com.thebund1st.tiantong.commands

import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiCreateOnlinePaymentRequest
import com.thebund1st.tiantong.wechatpay.qrcode.WeChatPayNativeCreateOnlinePaymentRequest
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

import static com.thebund1st.tiantong.commands.RequestOnlinePaymentCommandFixture.aRequestOnlinePaymentCommand

class CreateOnlinePaymentCommandValidatorTest extends Specification {

    private Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    @Unroll("The validation is #pass given the amount is #amount")
    def "it should fail the validation given the amount is negative"(double amount, boolean pass) {
        expect:
        CreateOnlinePaymentCommand command = aRequestOnlinePaymentCommand().amountIs(amount).build()
        def constraintViolations = validator.validate(command)
        assert constraintViolations.isEmpty() == pass

        where:
        amount | pass
        1.0    | true
        0      | false
        -1     | false
    }

    @Unroll("The provider specific request validation for WeChat Pay Native is #pass given the request is #request")
    def "it should be able to validate WeChat Pay native"(ProviderSpecificCreateOnlinePaymentRequest request,
                                                          boolean pass) {
        expect:
        CreateOnlinePaymentCommand command = aRequestOnlinePaymentCommand()
                .byWeChatPayNative()
                .with(request).build()
        def constraintViolations = validator.validate(command)
        assert constraintViolations.isEmpty() == pass

        where:
        request                                                         | pass
        null                                                            | false
        new WeChatPayNativeCreateOnlinePaymentRequest()                 | false
        new WeChatPayNativeCreateOnlinePaymentRequest(productId: "foo") | true
    }

    @Unroll("The provider specific request validation for WeChat Pay JsApi is #pass given the request is #request")
    def "it should be able to validate WeChat Pay JsApi"(ProviderSpecificCreateOnlinePaymentRequest request,
                                                         boolean pass) {
        expect:
        CreateOnlinePaymentCommand command = aRequestOnlinePaymentCommand()
                .byWeChatPayJsApi()
                .with(request).build()
        def constraintViolations = validator.validate(command)
        assert constraintViolations.isEmpty() == pass

        where:
        request                                                     | pass
        null                                                        | false
        new WeChatPayJsApiCreateOnlinePaymentRequest()              | false
        new WeChatPayJsApiCreateOnlinePaymentRequest(openId: "foo") | true
    }
}
