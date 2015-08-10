package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(JarSpecJUnitRunner.class)
public class ErrorInDescribeSpec implements Specification {

    @Override
    public SpecificationNode root() {
        return describe("Partially broken spec", by(
            describe("broken single-child unit", () -> {
                brokenSetupMethod();
                return it("doesn't get this far", () -> fail("should not run"));
            }),
            describe("broken multi-child unit", () -> {
                brokenSetupMethod();
                return byAllOf(
                        it("doesn't get this far", () -> fail("should not run")),
                        it("doesn't get this far", () -> fail("should not run")));
            }),
            describe("unit with unchecked Error", () -> {
                uncheckedErrorMethod();
                return it("doesn't get this far", () -> fail("should not run"));
            }),
            describe("successful unit", () ->
                it("passes", () -> assertTrue(true)))
        ));
    }

    private void brokenSetupMethod() throws Exception {
        throw new Exception("Setup failure thrown intentionally for test purposes");
    }

    private void uncheckedErrorMethod() {
        throw new Error("Setup failure thrown intentionally for test purposes");
    }
}
