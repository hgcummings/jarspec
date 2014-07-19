package io.hgc.jarspec.mixins;

import io.hgc.jarspec.SpecificationNode;
import io.hgc.jarspec.Test;

import java.util.Optional;

/**
 * Mixin providing convenience method for Specifications making statements about exception behaviour
 */
public interface ExceptionBehaviour {
    /**
     * Statement that a unit should throw a specific exception for a particular case
     *
     * @param throwable the class of the expected exception type
     * @param forCase   a description of the case in which it should be thrown
     * @param testCase  an implementation of the case in which is should be thrown
     * @param <T>       the expected exception type
     * @return a Specification representing the single description
     */
    default public <T extends Throwable> SpecificationNode itThrows(Class<T> throwable, String forCase, Test testCase) {
        return SpecificationNode.leaf(
            String.format("throws %s %s", throwable.getSimpleName(), forCase),
            Optional.of(() -> {
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
                            throwable.getSimpleName(), exception)
                    );
                }
            })
        );
    }
}