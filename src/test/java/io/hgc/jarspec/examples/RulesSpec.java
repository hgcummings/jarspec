package io.hgc.jarspec.examples;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class RulesSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("major unit",
            it("has a top-level statement", () ->
                System.out.println("  Top-level statement execution")),
            describe("nested minor unit",
                it("has a nested statement", () ->
                    System.out.println("    First nested statement execution")
                ).withRule(log("    Individual test rule")),
                it("has another nested statement",
                    () -> System.out.println("    Second nested statement execution"))
            ).withRule(log("  First nested rule"))
            .withRule(log("  Second nested rule"))
            .withBlockRule(log("  Nested block rule"))
        ).withRule(log("First top-level rule"))
        .withRule(log("Second top-level rule"))
        .withBlockRule(log("Top-level block rule"));
    }

    private static TestRule log(String name) {
        return new LoggingRule(name);
    }

    private static class LoggingRule extends ExternalResource {
        private final String name;

        private LoggingRule(String name) {
            this.name = name;
        }

        @Override
        public void before() {
            System.out.println(name + " before()");
        }

        @Override
        public void after() {
            System.out.println(name + " after()");
        }
    }
}