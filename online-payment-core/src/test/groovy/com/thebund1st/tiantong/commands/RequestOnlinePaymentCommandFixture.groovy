package com.thebund1st.tiantong.commands

import com.thebund1st.tiantong.core.OnlinePayment
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest
import com.thebund1st.tiantong.dummypay.DummyPayCreateOnlinePaymentRequest
import com.thebund1st.tiantong.utils.Randoms

import static com.thebund1st.tiantong.dummypay.DummyPayMethods.dummyPay
import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayJsApi
import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayNative

class RequestOnlinePaymentCommandFixture {
    private CreateOnlinePaymentCommand target = new CreateOnlinePaymentCommand()
    private Map providerSpecificInfo = [:]

    def amountIs(double amount) {
        target.setAmount(amount)
        this
    }

    def by(OnlinePayment.Method method) {
        target.setMethod(method.value)
        this
    }

    def byWeChatPayJsApi() {
        by(weChatPayJsApi())
    }

    def byWeChatPayNative() {
        by(weChatPayNative())
    }

    def byDummy() {
        by(dummyPay())
    }

    def correlatedWith(OnlinePayment.Correlation correlation) {
        target.setCorrelation(correlation)
        this
    }

    def withSubject(String value) {
        target.setSubject(value)
        this
    }

    def withBody(String value) {
        target.setBody(value)
        this
    }

    def withOpenId(String openId) {
        this.providerSpecificInfo['openId'] = openId
        this
    }

    def withDummySpecificInfo() {
        this.target.setProviderSpecificRequest(new DummyPayCreateOnlinePaymentRequest(dummy: 'dummy'))
        this
    }

    def with(ProviderSpecificCreateOnlinePaymentRequest request) {
        this.target.setProviderSpecificRequest(request)
        this
    }

    def build() {
        target
    }

    static def aRequestOnlinePaymentCommand() {
        new RequestOnlinePaymentCommandFixture()
                .amountIs(100.00)
                .byWeChatPayNative()
                .correlatedWith(OnlinePayment.Correlation.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
                .withSubject("Subject")
                .withBody("Body")
    }
}
