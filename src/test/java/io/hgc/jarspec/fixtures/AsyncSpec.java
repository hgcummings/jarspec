package io.hgc.jarspec.fixtures;


import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import io.hgc.jarspec.mixins.AsynchronousBehaviour;
import org.junit.runner.RunWith;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.assertEquals;

@RunWith(JarSpecJUnitRunner.class)
public class AsyncSpec implements Specification, AsynchronousBehaviour {
    @Override
    public SpecificationNode root() {
        return describe("asynchronous behaviour", () -> by (
            it("should complete within the timeout", () -> {
                AsyncResult result = new AsyncResult();
                asyncProcess(result);
                waitFor(() -> assertEquals(1, result.getValue()));
            }),
            it("should return the correct result", () -> {
                AsyncResult result = new AsyncResult();
                asyncProcess(result);
                waitFor(() -> assertEquals(2, result.getValue()));
            })
        ));
    }

    @Override
    public int getTimeoutMillis() {
        return 100;
    }

    private static void asyncProcess(AsyncResult result) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                result.setValue(1);
            }
        }, 10);
    }

    private static class AsyncResult {
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

}
