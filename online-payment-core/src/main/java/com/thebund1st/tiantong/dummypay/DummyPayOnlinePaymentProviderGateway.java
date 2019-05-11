package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator;
import com.thebund1st.tiantong.core.ProviderSpecificOnlinePaymentRequest;
import com.thebund1st.tiantong.core.ProviderSpecificRequest;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentProviderGateway;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentResultGateway;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;

@RequiredArgsConstructor
public class DummyPayOnlinePaymentProviderGateway implements
        MethodBasedOnlinePaymentProviderGateway, MethodBasedOnlinePaymentResultGateway {

    private final Clock clock;
    private final OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResultNotificationIdentifierGenerator;
    private List<OnlinePayment.Identifier> succeededOnlinePaymentGroup = new ArrayList<>();

    @Override
    public List<OnlinePayment.Method> matchedMethods() {
        return singletonList(OnlinePayment.Method.of("DUMMY_PAY"));
    }

    @Override
    public ProviderSpecificRequest request(OnlinePayment onlinePayment,
                                           ProviderSpecificOnlinePaymentRequest providerSpecificRequest) {
        DummyPaySpecificRequest dummyPaySpecificRequest = new DummyPaySpecificRequest();
        dummyPaySpecificRequest.setDummyId(UUID.randomUUID().toString());
        return dummyPaySpecificRequest;
    }

    @Override
    public boolean supports(String method) {
        return matchedMethods().contains(OnlinePayment.Method.of(method));
    }

    @Override
    public Optional<OnlinePaymentResultNotification> pull(OnlinePayment onlinePayment) {
        if (this.succeededOnlinePaymentGroup.contains(onlinePayment.getId())) {
            OnlinePaymentResultNotification notification = new OnlinePaymentResultNotification();
            notification.setId(onlinePaymentResultNotificationIdentifierGenerator.nextIdentifier());
            notification.setOnlinePaymentId(onlinePayment.getId());
            notification.setAmount(onlinePayment.getAmount());
            notification.setCode(OnlinePaymentResultNotification.Code.SUCCESS);
            notification.setCreatedAt(clock.now());
            return Optional.of(notification);
        } else {
            return Optional.empty();
        }
    }

    public void addSucceededOnlinePayment(OnlinePayment.Identifier onlinePaymentId) {
        this.succeededOnlinePaymentGroup.add(onlinePaymentId);
    }
}
