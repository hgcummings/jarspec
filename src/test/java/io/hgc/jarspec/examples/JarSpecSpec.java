package io.hgc.jarspec.examples;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class JarSpecSpec implements Specification {
    public JarSpecSpec() { System.out.println("constructor"); }

    @Override
    public SpecificationNode root() {
        System.out.println("root");
        return describe("Major unit", () -> {
            System.out.println("Major unit");
            return by(
                it("Top-level statement", () ->
                    System.out.println("Top-level statement")),
                describe("Minor unit", () -> {
                    System.out.println("Minor unit");
                    return it("Nested statement", () ->
                        System.out.println("Nested statement"));
                })
            );
        });
    }
}