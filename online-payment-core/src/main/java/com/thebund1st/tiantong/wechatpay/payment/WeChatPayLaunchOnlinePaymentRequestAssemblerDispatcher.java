package com.thebund1st.tiantong.wechatpay.payment;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thebund1st.tiantong.core.method.MethodMatcherFunction;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;
import com.thebund1st.tiantong.core.exceptions.NoSuchOnlinePaymentProviderGatewayException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WeChatPayLaunchOnlinePaymentRequestAssemblerDispatcher
        implements
        WeChatPayLaunchOnlinePaymentRequestAssembler<ProviderSpecificLaunchOnlinePaymentRequest>,
        MethodMatcherFunction<MethodBasedWeChatPayLaunchOnlinePaymentRequestAssembler, ProviderSpecificLaunchOnlinePaymentRequest> {

    private final List<MethodBasedWeChatPayLaunchOnlinePaymentRequestAssembler> assemblerGroup;

    @Override
    public ProviderSpecificLaunchOnlinePaymentRequest from(OnlinePayment onlinePayment, WxPayUnifiedOrderResult result) {
        return dispatch(assemblerGroup,
                onlinePayment::getMethod)
                .apply((assembler) -> assembler.from(onlinePayment, result),
                        () -> new NoSuchOnlinePaymentProviderGatewayException(onlinePayment));
    }
}
