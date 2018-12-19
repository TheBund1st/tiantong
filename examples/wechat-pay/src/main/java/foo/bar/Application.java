package foo.bar;


import com.thebund1st.tiantong.application.OnlinePaymentCommandHandler;
import com.thebund1st.tiantong.commands.MakeOnlinePaymentCommand;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

    @Autowired
    private WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway;
    @Autowired
    private OnlinePaymentCommandHandler onlinePaymentCommandHandler;

    @PostMapping("/api/notifications")
    public void handle(@RequestBody String command) {
        System.out.println(command);
    }

    @PostMapping("/api/online/payments")
    public String handle(@RequestBody MakeOnlinePaymentCommand command) {
        OnlinePayment op = onlinePaymentCommandHandler.handle(command);
        return weChatPayOnlinePaymentGateway.requestPayment(op).getQrCodeUri();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
