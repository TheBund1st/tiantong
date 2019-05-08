package com.thebund1st.tiantong.wechatpay;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WxPayUnifiedOrderRequestProviderSpecificRequestPopulatorDispatcher<P extends ProviderSpecificOnlinePaymentRequest>
        implements WxPayUnifiedOrderRequestProviderSpecificRequestPopulator<P> {
    private final List<MethodBasedWxPayUnifiedOrderRequestProviderSpecificRequestPopulator<P>> delegateGroup;

    @Override
    public void populate(WxPayUnifiedOrderRequest request,
                         P providerSpecificOnlinePaymentRequest) {
        delegateGroup.stream()
                .filter(p -> p.supports(request.getTradeType()))
                .findAny()
                .ifPresent(p -> p.populate(request, providerSpecificOnlinePaymentRequest));
    }
}
