package io.hgc.jarspec.mixins;

import io.hgc.jarspec.UnitSpec;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.Test;

public interface ExceptionBehaviour extends UnitSpec {
    default public  <T extends Throwable> Specification itThrows(Class<T> throwable, String forCase, Test testCase) {
        return it(String.format("should throw %s %s", throwable.getSimpleName(), forCase), () -> {
            Throwable exception = null;
            try {
                testCase.run();
            } catch (Throwable e) {
                exception = e;
            }
            if (!throwable.isInstance(exception)) {
                throw new AssertionError(
                    String.format(
                        "Expected exception of type %s but was %s",
                        throwable.getSimpleName(), exception));
            }
        });
    }
}