package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.rules.ErrorInRuleSpec;
import io.hgc.jarspec.fixtures.rules.ExpensiveDependencySpec;
import io.hgc.jarspec.fixtures.rules.SharedStateSpec;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JarSpecJUnitRunner.class)
public class RulesSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("TestRule behaviour",
                it("should apply rules correctly", () -> {
                    Result result = new JUnitCore().run(SharedStateSpec.class);
                    assertThat(result.getRunCount()).isEqualTo(3);
                    assertThat(result.getFailureCount()).isEqualTo(0);
                }),
                it("should apply block-level rules correctly", () -> {
                    Result result = new JUnitCore().run(ExpensiveDependencySpec.class);
                    assertThat(result.getRunCount()).isEqualTo(3);
                    assertThat(result.getFailureCount()).isEqualTo(0);
                }),
                it("should report failures in block-level rules as test errors", () -> {
                    Result result = new JUnitCore().run(ErrorInRuleSpec.class);
                    assertThat(result.getRunCount()).isEqualTo(1);
                    assertThat(result.getFailureCount()).isEqualTo(1);
                })
        );
    }
}
