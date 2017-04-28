package io.hgc.jarspec.mixins;

import io.hgc.jarspec.Test;

public interface AsynchronousBehaviour {
    int INITIAL_BACKOFF = 20;

    default void waitFor(Test test) throws Exception {
        long startTime = System.currentTimeMillis();
        int timeout = getTimeoutMillis();
        int backoff = INITIAL_BACKOFF;
        while (true) {
            try {
                test.run();
                return;
            } catch (AssertionError e) {
                if (System.currentTimeMillis() > startTime + timeout) {
                    throw new AssertionError("Assertion failed after " + timeout + "ms", e);
                }
                Thread.sleep(backoff += backoff >> 3);
            }
        }
    }

    default int getTimeoutMillis() {
        return 5000;
    }
}