package com.thebund1st.tiantong.wechatpay.payment;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.method.Method;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.core.method.MethodMatcherConsumer;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulatorDispatcher
        <P extends ProviderSpecificCreateOnlinePaymentRequest>
        implements WeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator<P>,
        MethodMatcherConsumer<MethodBasedWeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator<P>> {
    private final List<MethodBasedWeChatPayCreateOnlinePaymentRequestWxPayUnifiedOrderRequestPopulator<P>> delegateGroup;

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         P providerSpecificOnlinePaymentRequest) {
        dispatch(delegateGroup,
                () -> Method.of("WECHAT_PAY_" + request.getTradeType()),
                p -> p.populate(request, providerSpecificOnlinePaymentRequest));
    }
}
