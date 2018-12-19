package com.thebund1st.tiantong.wechatpay;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.thebund1st.tiantong.core.OnlinePayment;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class WeChatPayOnlinePaymentGateway {

    @Setter
    private String appId;
    @Setter
    private String merchantId;
    @Setter
    private String key;
    @Setter
    private String webhookEndpoint;
    @Setter
    private String baseUri = "https://api.mch.weixin.qq.com";

    private final RestTemplate restTemplate;

    @SneakyThrows
    public WeChatPayOrderResponse requestPayment(OnlinePayment op) {

        final Map<String, String> params = new HashMap<>();
        params.put("appid", appId);
        params.put("mch_id", merchantId);
        params.put("body", "欢迎订购");//FIXME should extract from OnlinePayment
        params.put("out_trade_no", op.getId().getValue());
        params.put("total_fee", String.valueOf(op.getAmount() * 100));
        params.put("trade_type", "NATIVE");//TODO should extract from OnlinePayment or dedicated gateway method
        params.put("notify_url", webhookEndpoint);
        params.put("spbill_create_ip", getLocalhostAddress());
        params.put("nonce_str", UUID.randomUUID().toString());
        params.put("sign", generateSignature(params, key));
        String res = restTemplate.postForObject(baseUri + "/pay/unifiedorder",
                toXml(params),
                String.class);
        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(res, WeChatPayOrderResponse.class);
    }

    @SneakyThrows
    private String getLocalhostAddress() {
        return InetAddress.getLocalHost().getHostAddress();
    }

    private String toXml(Map<String, String> params) {
        StringBuilder xml = new StringBuilder("<xml>");
        params.forEach((key1, value) -> {
            xml.append("<");
            xml.append(key1);
            xml.append("><![CDATA[");
            xml.append(value);
            xml.append("]]></");
            xml.append(key1);
            xml.append(">");
        });
        xml.append("</xml>");
        return xml.toString();
    }

    @SneakyThrows
    private String generateSignature(final Map<String, String> data, String key) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        return md5(sb.toString()).toUpperCase();
    }

    private String md5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
}
