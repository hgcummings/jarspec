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
        return describe("Selective execution", by(
                describe("unit containing a selected test", by(
                    it("does not execute other statement", () -> fail("should not run")),
                    it("executes selected statement", () -> assertTrue(true)).only()
                )),
                describe("other second-level unit", () -> {
                    fail("should not run");
                    return it("doesn't get this far", () -> fail("should not run"));
                }),
                describe("other second-level unit containing a selected test", by(
                    it("does not execute other statement", () -> fail("should not run")),
                    it("executes failing selected statement", () -> fail("expected failure")).only()
                ))
            )
        );
    }
}
