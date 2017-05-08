package io.hgc.jarspec.examples;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class JarSpecSpec implements Specification {
    public JarSpecSpec() { System.out.println("constructor"); }

    @Override
    public SpecificationNode root() {
        System.out.println("root initialisation");
        return describe("major unit", () -> {
            System.out.println("Major unit initialisation");
            return byAllOf(
                it("has a top-level statement", () ->
                    System.out.println("  Top-level statement execution")),
                describe("nested minor unit", () -> {
                    System.out.println("  Minor unit initialisation");
                    return byAllOf(
                        it("has a nested statement", () ->
                            System.out.println("    First nested statement execution")),
                        it("has another nested statement", () ->
                            System.out.println("    Second nested statement execution"))
                    );
                }).withRule(new ExternalResource() {
                    @Override
                    protected void before() throws Throwable {
                        System.out.println("  inner ExternalResource setup");
                    }

                    @Override
                    protected void after() {
                        System.out.println("  inner ExternalResource teardown");
                    }
                })
            );
        }).withRule(new ExternalResource() {
            @Override
            protected void before() throws Throwable {
                System.out.println("  outer ExternalResource setup");
            }

            @Override
            protected void after() {
                System.out.println("  outer ExternalResource teardown");
            }
        });
    }
}