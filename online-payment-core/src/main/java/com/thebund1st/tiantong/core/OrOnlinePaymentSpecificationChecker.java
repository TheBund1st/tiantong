package com.thebund1st.tiantong.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class OrOnlinePaymentSpecificationChecker<T> implements OnlinePaymentSpecificationChecker<T> {

    private final List<OnlinePaymentSpecificationChecker<T>> checkers;

    public OrOnlinePaymentSpecificationChecker(List<OnlinePaymentSpecificationChecker<T>> checkers) {
        this.checkers = checkers;
    }

    @SafeVarargs
    public OrOnlinePaymentSpecificationChecker(OnlinePaymentSpecificationChecker<T>... checkers) {
        this(Arrays.stream(checkers).collect(Collectors.toList()));
    }

    @Override
    public void accept(T t) {
        checkers.forEach(c -> c.accept(t));
    }
}
