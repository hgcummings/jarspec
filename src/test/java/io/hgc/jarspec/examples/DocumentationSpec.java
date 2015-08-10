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
        return describe("Documentation example", by(
            it("demonstrates Statement nodes", () -> assertTrue(true)),
            it("demonstrates multiple Statement nodes", () -> assertTrue(true)),
            describe("nested unit", by(
                it("demonstrates single-statement case", () -> assertTrue(true))
            ))
        ));
    }
}