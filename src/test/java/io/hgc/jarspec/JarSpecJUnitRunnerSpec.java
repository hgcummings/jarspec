package io.hgc.jarspec;

import io.hgc.jarspec.examples.AdditionSpec;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;

import java.util.List;
import java.util.function.Supplier;

import static io.hgc.jarspec.Specification.*;
import static org.junit.Assert.*;

@RunWith(JarSpecJUnitRunner.class)
public class JarSpecJUnitRunnerSpec implements Supplier<Specification> {
    @Override
    public Specification get() {
        return describe("JUnit Runner", () -> {
            Runner runner = new JarSpecJUnitRunner<>(AdditionSpec.class);
            return all(
                describe("constructor", () ->
                    it("should throw runtime exception for illegal access", () -> {
                        RuntimeException exception = null;
                        try {
                            new JarSpecJUnitRunner<>(PrivateTestClass.class);
                        } catch (RuntimeException e) {
                            exception = e;
                        }
                        assertNotNull(exception);
                    })
                ),
                describe("test count", () ->
                    it("should match number of tests", () -> {
                        assertEquals(2, runner.testCount());
                    })
                ),
                describe("description", () -> all(
                    it("class should match provided class", () -> {
                        verifyDescriptionTestClass(runner.getDescription(), AdditionSpec.class);
                    }),
                    it("text should include parent context", () -> {
                        Description description = runner.getDescription();
                        assertEquals("addition", description.getMethodName());

                        assertEquals(1, description.getChildren().size());
                        description = description.getChildren().get(0);
                        assertEquals("addition of 1+1", description.getMethodName());

                        assertEquals(2, description.getChildren().size());
                        assertEquals("addition of 1+1 should equal 2", description.getChildren().get(0).getMethodName());
                        assertEquals("addition of 1+1 should equal 3", description.getChildren().get(1).getMethodName());
                    })
                )),
                it("should report failures correctly", () -> {
                    JUnitCore jUnitCore = new JUnitCore();
                    Result result = jUnitCore.run(runner);
                    assertEquals(2, result.getRunCount());
                    assertEquals(1, result.getFailureCount());
                    Failure failure = result.getFailures().get(0);
                    assertEquals("addition of 1+1 should equal 3", failure.getDescription().getMethodName());
                })
            );
        });
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

    private static class PrivateTestClass implements Supplier<Specification> {
        @Override
        public Specification get() {
            return null;
        }
    }
}