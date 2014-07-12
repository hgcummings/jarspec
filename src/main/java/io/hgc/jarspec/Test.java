package io.hgc.jarspec;

/**
 * Represents a test of a description in a root
 */
@FunctionalInterface
public interface Test {
    /**
     * Implementation of a test for a single statement in a root.
     * Should throw an exception if the test fails.
     * @throws Exception
     */
    void run() throws Exception;
}