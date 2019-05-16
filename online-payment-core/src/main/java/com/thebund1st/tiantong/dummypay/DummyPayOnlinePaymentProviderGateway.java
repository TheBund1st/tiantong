package com.thebund1st.tiantong.dummypay;

import com.thebund1st.tiantong.core.OnlinePayment;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotification;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator;
import com.thebund1st.tiantong.core.payment.ProviderSpecificCreateOnlinePaymentRequest;
import com.thebund1st.tiantong.core.payment.ProviderSpecificLaunchOnlinePaymentRequest;
import com.thebund1st.tiantong.provider.MethodBasedCloseOnlinePaymentGateway;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentProviderGateway;
import com.thebund1st.tiantong.provider.MethodBasedOnlinePaymentResultGateway;
import com.thebund1st.tiantong.time.Clock;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.thebund1st.tiantong.core.OnlinePaymentResultNotification.Code.CLOSED;
import static com.thebund1st.tiantong.core.OnlinePaymentResultNotification.Code.SUCCESS;
import static com.thebund1st.tiantong.dummypay.DummyPayMethods.dummyPay;

@RequiredArgsConstructor
public class DummyPayOnlinePaymentProviderGateway implements
        MethodBasedOnlinePaymentProviderGateway,
        MethodBasedOnlinePaymentResultGateway,
        MethodBasedCloseOnlinePaymentGateway {

    private final Clock clock;
    private final OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResultNotificationIdentifierGenerator;
    private List<OnlinePayment.Identifier> succeededOnlinePaymentGroup = new ArrayList<>();
    private List<OnlinePayment.Identifier> closedOnlinePaymentGroup = new ArrayList<>();

    @Override
    public ProviderSpecificLaunchOnlinePaymentRequest create(OnlinePayment onlinePayment,
                                                             ProviderSpecificCreateOnlinePaymentRequest providerSpecificRequest) {
        DummyPayLaunchOnlinePaymentRequest dummyPaySpecificRequest = new DummyPayLaunchOnlinePaymentRequest();
        dummyPaySpecificRequest.setDummyId(UUID.randomUUID().toString());
        return dummyPaySpecificRequest;
    }

    @Override
    public boolean supports(OnlinePayment.Method method) {
        return dummyPay().equals(method);
    }

    @Override
    public Optional<OnlinePaymentResultNotification> pull(OnlinePayment onlinePayment) {
        if (this.succeededOnlinePaymentGroup.contains(onlinePayment.getId())) {
            return anOnlinePaymentResultNotification(onlinePayment, OnlinePaymentResultNotification.Code.SUCCESS);
        } else if (this.closedOnlinePaymentGroup.contains(onlinePayment.getId())) {
            return anOnlinePaymentResultNotification(onlinePayment, CLOSED);
        } else {
            return Optional.empty();
        }
    }

    private Optional<OnlinePaymentResultNotification> anOnlinePaymentResultNotification(OnlinePayment onlinePayment, OnlinePaymentResultNotification.Code code) {
        OnlinePaymentResultNotification notification = new OnlinePaymentResultNotification();
        notification.setId(onlinePaymentResultNotificationIdentifierGenerator.nextIdentifier());
        notification.setOnlinePaymentId(onlinePayment.getId());
        if (SUCCESS == code) {
            notification.setAmount(onlinePayment.getAmount());
        }
        notification.setCode(code);
        notification.setCreatedAt(clock.now());
        return Optional.of(notification);
    }

    public void addSucceededOnlinePayment(OnlinePayment.Identifier onlinePaymentId) {
        this.succeededOnlinePaymentGroup.add(onlinePaymentId);
    }

    @Override
    public void close(OnlinePayment onlinePayment) {
        closedOnlinePaymentGroup.add(onlinePayment.getId());
    }
}
