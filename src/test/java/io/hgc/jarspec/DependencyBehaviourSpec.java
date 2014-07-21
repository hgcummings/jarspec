package io.hgc.jarspec;

import io.hgc.jarspec.mixins.DependencyBehaviour;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

@RunWith(JarSpecJUnitRunner.class)
public class DependencyBehaviourSpec implements Specification {
    private static List<Dependency> methodOneTargets;
    private static List<Dependency> methodTwoTargets;
    private static List<Dependency> teardownTargets;

    @Override
    public SpecificationNode root() {
        return describe("dependency behaviour", () -> {
            methodOneTargets = new ArrayList<>();
            methodTwoTargets = new ArrayList<>();
            teardownTargets = new ArrayList<>();
            Runner runner = new JarSpecJUnitRunner<>(DependencySpec.class);
            Result result = new JUnitCore().run(runner);

            return by(
                it("creates new dependency for each test", () -> {
                    assertEquals(2, result.getRunCount());
                    assertEquals(0, result.getFailureCount());

                    assertEquals(1, methodOneTargets.size());
                    assertEquals(1, methodTwoTargets.size());

                    assertNotSame(methodOneTargets.get(0), methodTwoTargets.get(0));
                }),
                it("tears down the dependency after each test", () -> {
                    assertEquals(2, teardownTargets.size());
                    assertSame(methodOneTargets.get(0), teardownTargets.get(0));
                    assertSame(methodTwoTargets.get(0), teardownTargets.get(1));
                }));
        });
    }

    public static class DependencySpec implements Specification, DependencyBehaviour<Dependency> {
        @Override
        public SpecificationNode root() {
            return describe("dependant", () -> by(
                it("uses the first method of the dependency", Dependency::methodOne),
                it("uses the second method of the dependency", Dependency::methodTwo)
            ));
        }

        @Override
        public Dependency setupDependency() {
            return new Dependency();
        }

        @Override
        public void teardownDependency(Dependency dependency) {
            dependency.tearDown();
        }
    }

    private static class Dependency {
        public void methodOne() {
            methodOneTargets.add(this);
        }

        public void methodTwo() {
            methodTwoTargets.add(this);
        }

        public void tearDown() {
            teardownTargets.add(this);
        }
    }
}
