package com.thebund1st.tiantong.wechatpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "xml")
public class WeChatPayOrderResponse {
    @JsonProperty("code_url")
    private String qrCodeUri;
}
