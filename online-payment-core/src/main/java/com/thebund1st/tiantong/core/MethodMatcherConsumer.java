package com.thebund1st.tiantong.core;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface MethodMatcherConsumer<MethodAwareConsumer extends MethodMatcher> {

    default void dispatch(List<MethodAwareConsumer> methodAwareGroup,
                          Supplier<OnlinePayment.Method> methodSupplier,
                          Consumer<MethodAwareConsumer> consumer) {
        methodAwareGroup.stream()
                .filter(methodAware -> methodAware.supports(methodSupplier.get()))
                .findFirst()
                .ifPresent(consumer);
    }

    default void dispatch(List<MethodAwareConsumer> methodAwareGroup,
                          Supplier<OnlinePayment.Method> methodSupplier,
                          Consumer<MethodAwareConsumer> consumer,
                          Supplier<RuntimeException> error) {
        MethodAwareConsumer c = methodAwareGroup.stream()
                .filter(methodAware -> methodAware.supports(methodSupplier.get()))
                .findFirst().orElseThrow(error);
        consumer.accept(c);
    }

}
