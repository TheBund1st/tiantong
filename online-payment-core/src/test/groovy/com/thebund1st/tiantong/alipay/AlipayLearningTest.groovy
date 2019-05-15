package com.thebund1st.tiantong.alipay

import com.alipay.api.AlipayClient
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.request.AlipayTradePagePayRequest
import org.junit.Ignore
import spock.lang.Specification

@Ignore
class AlipayLearningTest extends Specification {

    AlipayClient alipayClient

    def setup() {
        alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                getAppId(), getPrivateKey(), "nativeJson", "UTF-8", getPublicKey(), "RSA2")
    }

    def "it should create unified order"() {
        given:
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest()//创建API对应的request
        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp")
        alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp")//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("""{
                "out_trade_no":"${UUID.randomUUID().toString().replace("-", "")}",
                "product_code":"FAST_INSTANT_TRADE_PAY",
                "total_amount":88.88,
                "subject":"Iphone6 16G",
                "body":"Iphone6 16G",
                "passback_params":"merchantBizType%3d3C%26merchantBizNo%3d2016010101111",
                "extend_params":{
                    "sys_service_provider_id":"2088511833207846"
                    }
                }
                """)//填充业务参数

        when:
        def res = alipayClient.pageExecute(alipayRequest)

        then:
        println(res.body)
    }

    String getPublicKey() {
        System.getenv("PUBLIC_KEY")
    }

    private String getPrivateKey() {
        System.getenv("PRIVATE_KEY")
    }

    private String getAppId() {
        System.getenv("APP_ID")
    }


}
