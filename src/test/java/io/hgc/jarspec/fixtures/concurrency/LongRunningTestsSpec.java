package io.hgc.jarspec.fixtures.concurrency;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class LongRunningTestsSpec implements Specification {
    public static final int TEST_DURATION_MILLIS = 100;

    @Override
    public SpecificationNode root() {
        return describe("Concurrent execution",
                it("first test", () -> Thread.sleep(TEST_DURATION_MILLIS)),
                it("second test", () -> Thread.sleep(TEST_DURATION_MILLIS)),
                it("third test", () -> Thread.sleep(TEST_DURATION_MILLIS)),
                it("fourth test", () -> Thread.sleep(TEST_DURATION_MILLIS))
        );
    }
}
