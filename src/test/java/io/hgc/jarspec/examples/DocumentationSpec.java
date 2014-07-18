package io.hgc.jarspec.examples;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(JarSpecJUnitRunner.class)
public class DocumentationSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("Documentation example", () -> by(
            it("should demonstrate Statement nodes", () -> assertTrue(true)),
            it("should demonstrate multiple Statement nodes", () -> assertTrue(true)),
            describe("nested unit", () ->
                it("should demonstrate single-statement case", () -> assertTrue(true))
            )
        ));
    }
}