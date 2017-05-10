package io.hgc.jarspec.fixtures.rules;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import static org.junit.Assert.assertTrue;

@RunWith(JarSpecJUnitRunner.class)
public class ErrorInRuleSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return it("has a passing test with a failing block rule", () -> assertTrue(true)).withBlockRule(ruleWithError);
    }

    private static TestRule ruleWithError = (base, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            throw new RuntimeException("Test exception thrown from rule");
        }
    };
}
