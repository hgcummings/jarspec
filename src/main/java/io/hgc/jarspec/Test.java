package io.hgc.jarspec;

/**
 * Represents a test of a statement in a specification
 */
@FunctionalInterface
public interface Test {
    /**
     * Implementation of a test for a single statement in a specification.
     * Should throw an exception if the test fails.
     * @throws Exception
     */
    void run() throws Exception;
}