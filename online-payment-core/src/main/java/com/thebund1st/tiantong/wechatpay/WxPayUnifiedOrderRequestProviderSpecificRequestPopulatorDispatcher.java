package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import com.thebund1st.tiantong.core.MethodMatcherConsumer;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WxPayUnifiedOrderRequestProviderSpecificRequestPopulatorDispatcher
        <P extends ProviderSpecificOnlinePaymentRequest>
        implements WxPayUnifiedOrderRequestProviderSpecificRequestPopulator<P>,
        MethodMatcherConsumer<MethodBasedWxPayUnifiedOrderRequestProviderSpecificRequestPopulator<P>> {
    private final List<MethodBasedWxPayUnifiedOrderRequestProviderSpecificRequestPopulator<P>> delegateGroup;

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         P providerSpecificOnlinePaymentRequest) {
        dispatch(delegateGroup,
                () -> OnlinePayment.Method.of("WECHAT_PAY_" + request.getTradeType()),
                p -> p.populate(request, providerSpecificOnlinePaymentRequest));
    }
}
