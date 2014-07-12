package io.hgc.jarspec;

/**
 * Represents a test of a description in a root
 */
@FunctionalInterface
public interface Test {
    /**
     * Implementation of a test for a single statement in a root.
     * @throws Exception indicates a test failure
     */
    void run() throws Exception;
}