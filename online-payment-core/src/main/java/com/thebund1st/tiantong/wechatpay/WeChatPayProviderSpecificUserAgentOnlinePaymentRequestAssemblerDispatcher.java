package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thebund1st.tiantong.core.MethodMatcherFunction;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.ProviderSpecificUserAgentOnlinePaymentRequest;
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssemblerDispatcher
        implements
        WeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler<ProviderSpecificUserAgentOnlinePaymentRequest>,
        MethodMatcherFunction<MethodBasedWeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler, ProviderSpecificUserAgentOnlinePaymentRequest> {

    private final List<MethodBasedWeChatPayProviderSpecificUserAgentOnlinePaymentRequestAssembler> assemblerGroup;

    @Override
    public ProviderSpecificUserAgentOnlinePaymentRequest from(OnlinePayment onlinePayment, WxPayUnifiedOrderResult result) {
        return dispatch(assemblerGroup,
                onlinePayment::getMethod)
                .apply((assembler) -> assembler.from(onlinePayment, result),
                        () -> new NoSuchOnlinePaymentProviderGatewayException(onlinePayment));
    }
}
