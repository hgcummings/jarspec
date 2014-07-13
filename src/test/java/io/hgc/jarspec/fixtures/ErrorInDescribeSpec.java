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
        return describe("partially broken spec", () -> by(
            describe("broken single-child describe", () -> {
                brokenSetupMethod();

                return it("shouldn't get this far", () -> {
                    fail("Test should not reach this point");
                });
            }),
            describe("broken multi-child describe", () -> {
                brokenSetupMethod();

                return by(it("shouldn't get this far", () -> {
                    fail("Test should not reach this point");
                }));
            }),
            describe("describe with unchecked Error", () -> {
                uncheckedErrorMethod();

                return by(it("shouldn't get this far", () -> {
                    fail("Test should not reach this point");
                }));
            }),
            describe("successful describe", () ->
                it("should pass", () -> assertTrue(true)))
        ));
    }

    private void brokenSetupMethod() throws Exception {
        throw new Exception("Setup failure thrown intentionally for test purposes");
    }

    private void uncheckedErrorMethod() {
        throw new Error("Setup failure thrown intentionally for test purposes");
    }
}
