package com.thebund1st.tiantong.wechatpay.webhooks;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.web.webhooks.NotifyPaymentResultCommandAssembler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class WeChatPayNotifyPaymentResultCommandAssembler implements NotifyPaymentResultCommandAssembler {
    private final WxPayService wxPayService;

    @SneakyThrows
    @Override
    public OnlinePaymentSuccessNotification from(String rawNotification) {
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(rawNotification);
        OnlinePaymentSuccessNotification notification = new OnlinePaymentSuccessNotification(
                OnlinePayment.Identifier.of(result.getOutTradeNo()),
                BigDecimal.valueOf(result.getTotalFee()).divide(BigDecimal.valueOf(100)).doubleValue(),
                result.getXmlString());
        return notification;
    }
}
