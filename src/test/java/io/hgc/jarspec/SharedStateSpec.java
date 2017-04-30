package io.hgc.jarspec;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JarSpecJUnitRunner.class)
public class SharedStateSpec implements Specification {
    @Override
    public SpecificationNode root() {
        final Counter counter = new Counter();
        return describe("Shared state",
                it("can mutate shared state", () -> {
                    assertNotNull(counter);
                    counter.increment();
                }),
                it("persists and resets shared state object", () -> {
                    counter.increment();
                    assertEquals(2, counter.persistentCount);
                    assertEquals(1, counter.count);
                })
        ).withReset(counter::reset);
    }

    public static class Counter {
        int count;
        int persistentCount;

        public void increment() {
            count++;
            persistentCount++;
        }

        public void reset() {
            count = 0;
        }
    }
}
