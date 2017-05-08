package io.hgc.jarspec;

import org.junit.runners.model.Statement;

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

    default Statement asStatement() {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                run();
            }
        };
    }
}