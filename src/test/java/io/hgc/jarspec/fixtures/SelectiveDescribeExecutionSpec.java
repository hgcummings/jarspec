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
        return describe("Selective execution", by(
            describe("top-level selection", by(
                it("executes statement", () -> assertTrue(true)),
                describe("nested unit", by(it ("executes nested statement", () -> assertTrue(true))))
            )).only(),
            describe("other top-level selection", by(
                it("executes statement", () -> assertTrue(true)))).only(),
            describe("other top-level unit", by(
                it("does not execute first statement", () -> fail("should not run")),
                it("does not execute second statement", () -> fail("should not run"))
            ))
        ));
    }
}
