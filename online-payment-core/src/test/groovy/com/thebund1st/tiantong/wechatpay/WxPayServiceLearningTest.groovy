package com.thebund1st.tiantong.wechatpay

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest
import com.github.binarywang.wxpay.config.WxPayConfig
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl
import spock.lang.Specification

class WxPayServiceLearningTest extends Specification {

    WxPayServiceImpl subject = new WxPayServiceImpl()
    WxPayConfig wxPayConfig = new WxPayConfig()

    def setup() {
        wxPayConfig.setAppId(getAppId())
        wxPayConfig.setMchId(getMerchantId())
        wxPayConfig.setMchKey(getSandboxKey())
        wxPayConfig.setUseSandboxEnv(true)
        subject.setConfig(wxPayConfig)
    }

    def "it should create unified order"() {
        given:
        WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest()
        req.setBody('BODY')
        req.setProductId("Product Id")
        req.setOutTradeNo('1415659990')
        req.setTotalFee(301)//fen
        req.setTradeType("NATIVE")
        req.setSpbillCreateIp("192.168.1.1")
        req.setNotifyUrl(getNotificationUri())
        req.setNonceStr('1add1a30ac87aa2db72f57a2375d8fec')
        when:
        def res = subject.unifiedOrder(req)

        then:
        println(res)
    }

    private String getKey() {
        System.getenv("KEY")
    }

    private String getNotificationUri() {
        System.getenv("NOTIFICATION_URI")
    }

    private String getMerchantId() {
        System.getenv("MERCHANT_ID")
    }

    private String getAppId() {
        System.getenv("APP_ID")
    }

    private String getSandboxKey() {
        System.getenv("SANDBOX_KEY")
    }

}
