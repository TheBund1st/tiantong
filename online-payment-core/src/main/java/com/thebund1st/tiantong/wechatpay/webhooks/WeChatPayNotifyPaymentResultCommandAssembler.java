package com.thebund1st.tiantong.wechatpay.webhooks;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.exceptions.FakeOnlinePaymentNotificationException;
import com.thebund1st.tiantong.web.webhooks.NotifyPaymentResultCommandAssembler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class WeChatPayNotifyPaymentResultCommandAssembler implements NotifyPaymentResultCommandAssembler {
    private static final String SUCCESS = "SUCCESS";
    private final WxPayService wxPayService;

    @SneakyThrows
    @Override
    public NotifyPaymentResultCommand from(String rawNotification) {
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(rawNotification);
        if (SUCCESS.equals(result.getReturnCode())) {
            NotifyPaymentResultCommand notification = new NotifyPaymentResultCommand(
                    OnlinePayment.Identifier.of(result.getOutTradeNo()),
                    BigDecimal.valueOf(result.getTotalFee()).divide(BigDecimal.valueOf(100)).doubleValue(),
                    SUCCESS.equals(result.getResultCode()), result.getXmlString());
            return notification;
        } else {
            throw new FakeOnlinePaymentNotificationException(rawNotification);
        }
    }
}
