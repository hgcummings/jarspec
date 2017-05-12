package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.concurrency.LongRunningTestsSpec;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JarSpecJUnitRunner.class)
public class ConcurrencySpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("Concurrent execution",
                it("Executes tests concurrently by default", () -> {
                    long startTimeMills = System.currentTimeMillis();
                    Result result = new JUnitCore().run(LongRunningTestsSpec.class);
                    long duration = System.currentTimeMillis() - startTimeMills;

                    assertThat(result.getRunCount()).isEqualTo(4);
                    assertThat(result.getFailureCount()).isEqualTo(0);

                    assertThat(duration).isLessThan(LongRunningTestsSpec.TEST_DURATION_MILLIS * 2);
                }));
    }


    long time(Runnable runnable) {
        long startTimeMills = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - startTimeMills;
    }
}
