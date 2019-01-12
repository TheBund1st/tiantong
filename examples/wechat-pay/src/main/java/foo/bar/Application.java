package foo.bar;


import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.thebund1st.tiantong.application.OnlinePaymentNotificationSubscriber;
import com.thebund1st.tiantong.application.RequestOnlinePaymentCommandHandler;
import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification;
import com.thebund1st.tiantong.commands.RequestOnlinePaymentCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@SpringBootApplication
@Slf4j
public class Application {

    @Autowired
    private WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway;
    @Autowired
    private RequestOnlinePaymentCommandHandler requestOnlinePaymentCommandHandler;
    @Autowired
    private OnlinePaymentNotificationSubscriber onlinePaymentNotificationSubscriber;
    @Autowired
    private WxPayService wxPayService;

    @PostMapping("/api/notifications")
    public String handle(@RequestBody String command) throws WxPayException {
        final WxPayOrderNotifyResult notifyResult = this.wxPayService.parseOrderNotifyResult(command);
        OnlinePaymentSuccessNotification event =
                new OnlinePaymentSuccessNotification(
                        OnlinePayment.Identifier.of(notifyResult.getOutTradeNo()),
                        BigDecimal.valueOf(notifyResult.getTotalFee()).divide(BigDecimal.valueOf(100)).doubleValue(),
                        command);
        onlinePaymentNotificationSubscriber.handle(event);
        // TODO 根据自己业务场景需要构造返回对象
        log.info(notifyResult.toString());
        return WxPayNotifyResponse.success("成功");
    }

    @PostMapping("/api/online/payments")
    public String handle(@RequestBody RequestOnlinePaymentCommand command) {
        OnlinePayment op = requestOnlinePaymentCommandHandler.handle(command);
        return weChatPayOnlinePaymentGateway.requestPayment(op).getQrCodeUri();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
