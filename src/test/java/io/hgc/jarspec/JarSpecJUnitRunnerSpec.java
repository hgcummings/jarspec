package io.hgc.jarspec;

import io.hgc.jarspec.examples.AdditionSpec;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(JarSpecJUnitRunner.class)
public class JarSpecJUnitRunnerSpec implements Specification, ExceptionBehaviour {
    @Override
    public SpecificationNode root() {
        return describe("JUnit runner", () -> {
            Runner runner = new JarSpecJUnitRunner<>(AdditionSpec.class);
            return by(
                describe("constructor", () ->
                        itThrows(RuntimeException.class, "for inaccessible test class", () -> {
                            new JarSpecJUnitRunner<>(InaccessibleTestClass.class);
                            fail("This line should not be reached");
                        })
                ),
                describe("test count", () ->
                        it("matches number of tests", () -> assertEquals(2, runner.testCount()))
                ),
                describe("description", () -> by(
                    it("returns correct test class for all tests", () ->
                        verifyDescriptionTestClass(runner.getDescription(), AdditionSpec.class)),
                    it("includes parent context in test names", () -> {
                        Description description = runner.getDescription();
                        assertEquals("addition", description.getMethodName());

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

    public static class InaccessibleTestClass implements Specification {
        private InaccessibleTestClass() {
        }

        @Override
        public SpecificationNode root() {
            return it("should be true", () -> assertTrue(true));
        }
    }
}