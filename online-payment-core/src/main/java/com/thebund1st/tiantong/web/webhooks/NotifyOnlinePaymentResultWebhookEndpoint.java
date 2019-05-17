package com.thebund1st.tiantong.web.webhooks;

import com.thebund1st.tiantong.application.NotifyOnlinePaymentResultCommandHandler;
import com.thebund1st.tiantong.commands.NotifyPaymentResultCommand;
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

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class NotifyOnlinePaymentResultWebhookEndpoint extends OncePerRequestFilter {

    private final NotifyOnlinePaymentResultCommandAssembler notifyOnlinePaymentResultCommandAssembler;
    private final NotifyOnlinePaymentResultCommandHandler handler;
    private final NotifyOnlinePaymentResultResponseBodyAssembler notifyOnlinePaymentResultResponseBodyAssembler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String requestBody = IOUtils.toString(request.getInputStream(), Charset.forName("UTF-8"));
            log.info(requestBody);
            NotifyPaymentResultCommand command = notifyOnlinePaymentResultCommandAssembler.from(requestBody);
            handler.handle(command);
            response.getWriter().write(notifyOnlinePaymentResultResponseBodyAssembler.toResponseBody());
        } catch (Exception err) {
            log.error(err.getMessage(), err);
            response.sendError(SC_INTERNAL_SERVER_ERROR, err.getMessage());
        }
    }


}
