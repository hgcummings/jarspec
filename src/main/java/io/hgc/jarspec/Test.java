package io.hgc.jarspec;

/**
 * Represents a test for a statement in a specification.
 */
@FunctionalInterface
public interface Test {
    /**
     * Implementation of a test for a single statement in a specification.
     * @throws Exception indicating a test failure
     */
    void run() throws Exception;
}