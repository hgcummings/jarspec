package io.hgc.jarspec.mixins;

import io.hgc.jarspec.SpecificationNode;
import io.hgc.jarspec.Test;
import org.junit.rules.ExpectedException;

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
    default <T extends Throwable> SpecificationNode itThrows(Class<T> throwable, String forCase, Test testCase) {
        ExpectedException expectedException = ExpectedException.none();
        expectedException.expect(throwable);
        return SpecificationNode.leaf(
            String.format("throws %s %s", throwable.getSimpleName(), forCase),
            testCase).withRule(expectedException);
    }
}