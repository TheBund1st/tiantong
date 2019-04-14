package foo.bar;

import com.thebund1st.tiantong.core.DomainEventPublisher;
import com.thebund1st.tiantong.events.OnlinePaymentSucceededEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DomainEventPublisherStub implements DomainEventPublisher {
    private List<Object> events = new ArrayList<>();

    @Override
    public void publish(Object event) {
        events.add(event);
    }

    public Optional<OnlinePaymentSucceededEvent> shouldReceivePaymentSucceedEvent(final String onlinePaymentId) {
        return this.events.stream()
                .filter(e -> e instanceof OnlinePaymentSucceededEvent)
                .map(e->(OnlinePaymentSucceededEvent)e)
                .filter(e -> e.getOnlinePaymentId().getValue().equals(onlinePaymentId))
                .findFirst();
    }

}
