package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.rules.ErrorInRuleSpec;
import io.hgc.jarspec.fixtures.rules.ExpensiveDependencySpec;
import io.hgc.jarspec.fixtures.rules.SharedStateSpec;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JarSpecJUnitRunner.class)
public class RulesSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("TestRule behaviour",
                it("should apply rules correctly", () -> {
                    Result result = new JUnitCore().run(SharedStateSpec.class);
                    assertEquals(3, result.getRunCount());
                    assertEquals(0, result.getFailureCount());
                }),
                it("should apply block-level rules correctly", () -> {
                    Result result = new JUnitCore().run(ExpensiveDependencySpec.class);
                    assertEquals(3, result.getRunCount());
                    assertEquals(0, result.getFailureCount());
                }),
                it("should report failures in block-level rules as test errors", () -> {
                    Result result = new JUnitCore().run(ErrorInRuleSpec.class);
                    assertEquals(1, result.getRunCount());
                    assertEquals(1, result.getFailureCount());
                })
        );
    }
}
