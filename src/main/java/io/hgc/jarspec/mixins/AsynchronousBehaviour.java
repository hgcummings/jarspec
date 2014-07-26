package io.hgc.jarspec.mixins;

import io.hgc.jarspec.Test;

public interface AsynchronousBehaviour {
    static final int INITIAL_BACKOFF = 20;

    public default void waitFor(Test test) throws Exception {
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

    public default int getTimeoutMillis() {
        return 5000;
    }
}