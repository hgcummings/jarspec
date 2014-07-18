package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(JarSpecJUnitRunner.class)
public class SelectiveTestExecutionSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("test in", () -> by(
                describe("second-level describe containing a selected test", () -> by(
                    it("another test in same describe", () -> fail("should not run")),
                    it("for first selected statement", () -> assertTrue(true)).only()
                )),
                describe("other second-level describe", () -> {
                    fail("should not run");
                    return it("should not run", () -> fail("should not run"));
                }),
                describe("other second-level describe containing a selected test", () -> by(
                    it("another test in same describe", () -> fail("should not run")),
                    it("failing selected statement", () -> fail("expected failure")).only()
                ))
            )
        );
    }
}
