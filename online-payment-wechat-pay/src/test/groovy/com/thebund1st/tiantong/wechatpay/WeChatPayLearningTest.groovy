package com.thebund1st.tiantong.wechatpay

import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import spock.lang.Ignore
import spock.lang.Specification

import java.nio.charset.Charset
import java.security.MessageDigest

//@Ignore
class WeChatPayLearningTest extends Specification {

    RestTemplate restTemplate = new RestTemplate()

    def setup() {
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")))
    }

    def "it should create unified order"() {
        given:
        def params = [
                'appid'           : getAppId(),
                'mch_id'          : getMerchantId(),
                'attach'          : '应该回传',
                'body'            : 'JSAPI支付测试',
                'out_trade_no'    : '1415659990',
                'nonce_str'       : '1add1a30ac87aa2db72f57a2375d8fec',
                'notify_url'      : getNotificationUri(),
                'spbill_create_ip': '14.23.150.211',
                'total_fee'       : "301",
                'trade_type'      : 'NATIVE',
        ]


        and:
        params.put("sign", generateSignature(params, getKey()))

        and:
        def xml = toXml(params)
        println(xml)

        when:
        def res = restTemplate.postForObject('https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder',
                xml, String)

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

    def "it should get signkey"() {
        given:
        def params = [
                'mch_id'   : getMerchantId(),
                'nonce_str': '1add1a30ac87aa2db72f57a2375d8fec'
        ]
        and:
        params.put("sign", generateSignature(params, getSandboxKey()))

        and:
        def xml = toXml(params)
        println(xml)

        when:
        def res = restTemplate.postForEntity("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", xml, String)

        then:
        println(res)
    }

    static String toXml(Map params) {
        def xml = "<xml>"
        params.entrySet().forEach { it ->
            xml += '<' + it.key + '><![CDATA[' + it.value + ']]></' + it.key + '>'
        }
        xml += '</xml>'
        xml
    }

    static String generateSignature(final Map<String, String> data, String key) throws Exception {
        Set<String> keySet = data.keySet()
        String[] keyArray = keySet.toArray(new String[keySet.size()])
        Arrays.sort(keyArray)
        StringBuilder sb = new StringBuilder()
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&")
        }
        sb.append("key=").append(key)
        return md5(sb.toString()).toUpperCase()
    }

    static String md5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("md5")
        byte[] array = md.digest(data.getBytes("UTF-8"))
        StringBuilder sb = new StringBuilder()
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3))
        }
        return sb.toString().toUpperCase()
    }


}
