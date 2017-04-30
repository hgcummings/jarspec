package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(JarSpecJUnitRunner.class)
public class ErrorInDescribeSpec implements Specification {

    @Override
    public SpecificationNode root() {
        return describe("Partially broken spec",
            describe("broken single-child unit", () -> {
                brokenSetupMethod();
                return it("doesn't get this far", () -> {});
            }),
            describe("broken multi-child unit", () -> {
                brokenSetupMethod();
                return byAllOf(
                        it("doesn't get this far", () -> {}),
                        it("doesn't get this far", () -> {}));
            }),
            describe("unit with unchecked Error", () -> {
                uncheckedErrorMethod();
                return it("doesn't get this far", () -> {});
            }),
            describe("successful unit", () ->
                it("passes", () -> assertTrue(true)))
        );
    }

    private void brokenSetupMethod() throws Exception {
        throw new Exception("Setup failure thrown intentionally for test purposes");
    }

    private void uncheckedErrorMethod() {
        throw new Error("Setup failure thrown intentionally for test purposes");
    }
}
