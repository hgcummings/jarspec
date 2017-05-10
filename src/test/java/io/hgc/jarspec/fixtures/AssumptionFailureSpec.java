package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

@RunWith(JarSpecJUnitRunner.class)
public class AssumptionFailureSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("assumptions",
                it("assumes the worse", () -> assumeTrue(false)),
                it("assumes a rule", () -> assertTrue(true)).withRule(checkAssumptionRule),
                it("assumes a block rule", () -> assertTrue(true)).withBlockRule(checkAssumptionRule),
                it("fails if assumption doesn't hold", () -> assertTrue(false)).withRule(checkAssumptionRule)
        );
    }

    private static TestRule checkAssumptionRule = (base, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            assumeTrue(false);
            base.evaluate();
        }
    };
}
