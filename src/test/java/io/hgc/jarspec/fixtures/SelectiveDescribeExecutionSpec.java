package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(JarSpecJUnitRunner.class)
public class SelectiveDescribeExecutionSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("test for", () -> by(
            describe("top-level selection", () -> by(
                it("first statement", () -> assertTrue(true)),
                describe("nested unit", () -> it ("nested statement", () -> assertTrue(true)))
            )).only(),
            describe("top-level failing selection", () -> {
                brokenSetupMethod();
                return it("unreached statement", () -> fail("should not run"));
            }).only(),
            describe("other top-level describe", () -> by(
                it("first statement", () -> fail("should not run")),
                it("second statement", () -> fail("should not run"))
            ))
        ));
    }

    private void brokenSetupMethod() throws Exception {
        throw new Exception("Setup failure thrown intentionally for test purposes");
    }
}
