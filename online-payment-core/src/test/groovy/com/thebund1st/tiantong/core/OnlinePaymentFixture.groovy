package com.thebund1st.tiantong.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.thebund1st.tiantong.core.method.Method
import com.thebund1st.tiantong.core.payable.Payable
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest
import com.thebund1st.tiantong.utils.Randoms
import com.thebund1st.tiantong.wechatpay.jsapi.WeChatPayJsApiCreateOnlinePaymentRequest

import java.time.Duration
import java.time.LocalDateTime

import static com.thebund1st.tiantong.core.OnlinePaymentResponseFixture.anOnlinePaymentResponse
import static com.thebund1st.tiantong.wechatpay.WeChatPayMethods.weChatPayJsApi
import static java.time.LocalDateTime.now

class OnlinePaymentFixture {
    private OnlinePayment target
    private Map providerSpecificInfo = [:]

    OnlinePaymentFixture() {
        this.target = new OnlinePayment()
        target.setCreatedAt(now())
        target.setLastModifiedAt(now())
    }

    def idIs(String value) {
        idIs(OnlinePayment.Identifier.of(value))
    }

    def idIs(OnlinePayment.Identifier id) {
        target.setId(id)
        this
    }

    def amountIs(double amount) {
        target.setAmount(amount)
        this
    }

    def by(Method method) {
        target.setMethod(method)
        this
    }

    def byMethod(String method) {
        by(Method.of(method))
    }

    def correlatedWith(OnlinePayment.Correlation correlation) {
        target.setCorrelation(correlation)
        this
    }

    def with(Payable payable) {
        target.setPayable(payable)
        this
    }

    def succeeded() {
        def event = anOnlinePaymentResponse()
                .to(target.getId())
                .amountIs(target.getAmount())
                .succeeded()
                .build()
        target.on(event)
        this
    }

    def failed() {
        this
    }

    def subjectIs(String value) {
        target.setSubject(value)
        this
    }

    def bodyIs(String value) {
        target.setBody(value)
        this
    }

    def withOpenId(String openId) {
        this.providerSpecificInfo['openId'] = openId
        this
    }

    def withProductId(String value) {
        this.providerSpecificInfo['productId'] = value
        this
    }

    def withProviderSpecificInfo(Map providerSpecificInfo) {
        this.providerSpecificInfo = providerSpecificInfo
        this
    }

    def with(ProviderSpecificCreateOnlinePaymentRequest request) {
        this.target.setProviderSpecificOnlinePaymentRequest(request)
        this
    }

    def createdAt(LocalDateTime localDateTime) {
        this.target.setCreatedAt(localDateTime)
        this
    }

    def expires(Duration duration) {
        this.target.expires(duration)
        this
    }

    def build() {
        target.setProviderSpecificInfo(new ObjectMapper().writeValueAsString(providerSpecificInfo))
        target
    }

    static def anOnlinePayment() {
        new OnlinePaymentFixture()
                .idIs(OnlinePayment.Identifier.of(Randoms.randomStr()))
                .amountIs(100.00)
                .by(weChatPayJsApi())
                .correlatedWith(OnlinePayment.Correlation.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
                .with(Payable.of("E-COMMERCE-ORDERS", Randoms.randomStr()))
                .subjectIs("This is a test product")
                .bodyIs("This is a test product details")
                .withOpenId(Randoms.randomStr())
                .with(new WeChatPayJsApiCreateOnlinePaymentRequest(openId: Randoms.randomStr()))
                .expires(Duration.ofMinutes(30))
    }
}
