package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

@RunWith(JarSpecJUnitRunner.class)
public class SkippedTestSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("skipped", () -> by(
            describe("a describe marked as skipped", () ->
                it("has a failing test", () -> fail("skipped test should not run")
            )).skip(),
            it("is a statement with no test"),
            it("is a statement marked as skipped", () -> fail("skipped test should not run")).skip()
        ));
    }
}
