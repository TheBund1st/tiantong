package com.thebund1st.tiantong.core.method;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface MethodMatcherFunction<MethodMatcherFunction extends MethodMatcher, R> {

    default BiFunction<Function<MethodMatcherFunction, R>, Supplier<RuntimeException>, R> dispatch(
            List<MethodMatcherFunction> methodAwareGroup,
            Supplier<Method> methodSupplier) {
        Optional<MethodMatcherFunction> MethodMatcherFunction = methodAwareGroup.stream()
                .filter(methodAware -> methodAware.supports(methodSupplier.get()))
                .findFirst();
        return (f, e) -> {
            if (MethodMatcherFunction.isPresent()) {
                return f.apply(MethodMatcherFunction.get());
            } else {
                throw e.get();
            }
        };
    }

    default BiFunction<Function<MethodMatcherFunction, R>, Supplier<R>, R> dispatchOrElse(
            List<MethodMatcherFunction> methodAwareGroup,
            Supplier<Method> methodSupplier) {
        Optional<MethodMatcherFunction> MethodMatcherFunction = methodAwareGroup.stream()
                .filter(methodAware -> methodAware.supports(methodSupplier.get()))
                .findFirst();
        return (f, e) -> {
            if (MethodMatcherFunction.isPresent()) {
                return f.apply(MethodMatcherFunction.get());
            } else {
                return e.get();
            }
        };
    }

}
