package com.thebund1st.tiantong.wechatpay.webhooks;

import com.thebund1st.tiantong.application.OnlinePaymentNotificationSubscriber;
import com.thebund1st.tiantong.commands.OnlinePaymentSuccessNotification;
import com.thebund1st.tiantong.web.webhooks.NotifyPaymentResultCommandAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
@RequiredArgsConstructor
public class WeChatPayPaymentResultNotificationWebhookEndpoint extends OncePerRequestFilter {

    private static final String RESPONSE = "<xml>\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";

    private final NotifyPaymentResultCommandAssembler notifyPaymentResultCommandAssembler;
    private final OnlinePaymentNotificationSubscriber handler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String xmlData = IOUtils.toString(request.getInputStream(), Charset.forName("UTF-8"));
        log.info(xmlData);
        try {
            OnlinePaymentSuccessNotification command = notifyPaymentResultCommandAssembler.from(xmlData);
            handler.handle(command);
            response.getWriter().write(RESPONSE);
        } catch (Exception err) {
            log.error(err.getMessage(), err);
        }
    }


}
