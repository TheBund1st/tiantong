package com.thebund1st.tiantong.dummypay.webhooks;

import com.thebund1st.tiantong.application.NotifyPaymentResultCommandHandler;
import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;
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
public class DummyPaymentResultNotificationWebhookEndpoint extends OncePerRequestFilter {

    private static final String RESPONSE = "{\"result\":\"OK\"}";

    private final NotifyPaymentResultCommandAssembler notifyPaymentResultCommandAssembler;
    private final NotifyPaymentResultCommandHandler handler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String xmlData = IOUtils.toString(request.getInputStream(), Charset.forName("UTF-8"));
        log.info(xmlData);
        try {
            NotifyPaymentResultCommand command = notifyPaymentResultCommandAssembler.from(xmlData);
            handler.handle(command);
            response.getWriter().write(RESPONSE);
        } catch (Exception err) {
            log.error(err.getMessage(), err);
        }
    }


}