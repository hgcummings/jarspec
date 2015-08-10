package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.AsyncSpec;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;

import static org.junit.Assert.assertEquals;

@RunWith(JarSpecJUnitRunner.class)
public class AsynchronousBehaviourSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("asynchronous behaviour", by(
                it("should measure success after async process completes", () -> {
                    Result result = new JUnitCore().run(AsyncSpec.class);
                    assertEquals(2, result.getRunCount());
                    assertEquals(1, result.getFailureCount());
                })
            )
        );
    }
}
