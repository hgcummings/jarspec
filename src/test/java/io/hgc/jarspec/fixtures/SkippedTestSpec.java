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
        return describe("Top-level unit", by(
            describe("skipped unit", by(
                it("does not run nested tests", () -> fail("should not run"))
            )).skip(),
            it("has no test"),
            it("is marked as skipped", () -> fail("should not run")).skip()
        ));
    }
}
