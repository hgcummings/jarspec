package io.hgc.jarspec;

import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JarSpecJUnitRunner.class)
public class SharedStateSpec implements Specification {
    @Override
    public SpecificationNode root() {
        final Counter counter = new Counter();
        return describe("Reset",
                it("given modified shared state object", () -> {
                    assertNotNull(counter);
                    counter.increment();
                }),
                it("resets shared state object", () -> {
                    counter.increment();
                    assertEquals(2, counter.persistentCount);
                    assertEquals(1, counter.count);
                    assertEquals(2, counter.resetCount);
                }),
                it("given a statement with no test"),
                it("only resets before statements with tests", () -> {
                    assertEquals(3, counter.resetCount);
                })
        ).withRule(new ExternalResource() {
            @Override
            protected void before() throws Throwable {
                counter.reset();
            }
        });
    }

    public static class Counter {
        int count;
        int persistentCount;
        int resetCount;

        public void increment() {
            count++;
            persistentCount++;
        }

        public void reset() {
            count = 0;
            resetCount++;
        }
    }
}
