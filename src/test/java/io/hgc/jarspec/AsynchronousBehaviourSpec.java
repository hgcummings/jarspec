package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.async.AsyncSpec;
import io.hgc.jarspec.fixtures.async.AsyncTimeoutSpec;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JarSpecJUnitRunner.class)
public class AsynchronousBehaviourSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("asynchronous behaviour",
                it("should measure success or failure after async process completes", () -> {
                    Result result = new JUnitCore().run(AsyncSpec.class);
                    assertThat(result.getRunCount()).isEqualTo(2);
                    assertThat(result.getFailureCount()).isEqualTo(1);
                }),
                it("should report failure for work that times out", () -> {
                    Result result = new JUnitCore().run(AsyncTimeoutSpec.class);
                    assertThat(result.getRunCount()).isEqualTo(2);
                    assertThat(result.getFailureCount()).isEqualTo(1);
                })
            );
    }
}
