package com.thebund1st.tiantong.core;

public interface OnlinePaymentResultNotificationRepository {

    void save(OnlinePaymentResultNotification model);

    OnlinePaymentResultNotification mustFindBy(OnlinePaymentResultNotification.Identifier id);
}
