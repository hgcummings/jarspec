package io.hgc.jarspec;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.JarSpecTest;
import io.hgc.jarspec.Specification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import javax.management.Descriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static io.hgc.jarspec.Specification.describe;
import static io.hgc.jarspec.Specification.describes;
import static io.hgc.jarspec.Specification.it;
import static org.junit.Assert.assertEquals;

public class JarSpecJUnitRunnerTest {
    private Runner runner;

    @Before
    public void setup() {
        runner = new JarSpecJUnitRunner<>(TestClass.class);
    }

    @Test
    public void testCountShouldMatchNumberOfTests() throws InitializationError {
        assertEquals(2, runner.testCount());
    }

    @Test
    public void descriptionTestClassShouldMatchProvidedClass() {
        Description description = runner.getDescription();

        verifyDescriptionTestClass(description, TestClass.class);
    }

    @Test
    public void descriptionTextShouldIncludeParentContext() {
        Description description = runner.getDescription();
        assertEquals("addition", description.getMethodName());

        assertEquals(1, description.getChildren().size());
        description = description.getChildren().get(0);
        assertEquals("addition of 1+1", description.getMethodName());

        assertEquals(2, description.getChildren().size());
        assertEquals("addition of 1+1 should equal 2", description.getChildren().get(0).getMethodName());
        assertEquals("addition of 1+1 should equal 3", description.getChildren().get(1).getMethodName());
    }

    @Test
    public void reportsFailuresCorrectly() {
        JUnitCore jUnitCore = new JUnitCore();
        Result result = jUnitCore.run(runner);
        assertEquals(2, result.getRunCount());
        assertEquals(1, result.getFailureCount());
        Failure failure = result.getFailures().get(0);
        assertEquals("addition of 1+1 should equal 3", failure.getDescription().getMethodName());
    }

    protected static void verifyDescriptionTestClass(Description description, Class testClass) {
        assertEquals(testClass, description.getTestClass());

        List<Description> children = description.getChildren();

        if (children.size() > 0) {
            for (Description child : children) {
                verifyDescriptionTestClass(child, testClass);
            }
        }
    }

    public static class TestClass implements Supplier<Specification> {
        @Override
        public Specification get() {
            return describe("addition", () -> {
                return describes("of 1+1", () -> {
                    int result = 1 + 1;

                    return new Specification[]{
                        it("should equal 2", () -> {
                            assertEquals(2, result);
                        }),
                        it("should equal 3", () -> {
                            assertEquals(3, result);
                        })
                    };
                });
            });
        }
    }
}