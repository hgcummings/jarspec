package io.hgc.jarspec;

import io.hgc.jarspec.examples.AdditionSpec;
import io.hgc.jarspec.examples.AssumptionFailureSpec;
import io.hgc.jarspec.examples.ErrorInDescribeSpec;
import io.hgc.jarspec.examples.ErrorInRootSpec;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Using {@link io.hgc.jarspec.examples.AdditionSpec} to test the main runner class
 */
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
                        assertEquals("addition of 1+1 equals 2", description.getChildren().get(0).getMethodName());
                        assertEquals("addition of 1+1 equals 3", description.getChildren().get(1).getMethodName());
                    })
                )),
                it("reports failures correctly", () -> {
                    JUnitCore jUnitCore = new JUnitCore();
                    Result result = jUnitCore.run(runner);
                    assertEquals(2, result.getRunCount());
                    assertEquals(1, result.getFailureCount());
                    Failure failure = result.getFailures().get(0);
                    assertEquals("addition of 1+1 equals 3", failure.getDescription().getMethodName());
                }),
                it("does not report assumption failures as test failures", () -> {
                    Result result = new JUnitCore().run(AssumptionFailureSpec.class);
                    assertEquals(0, result.getFailureCount());
                }),
                describe("setup error", () -> {
                    Result result = new JUnitCore().run(ErrorInDescribeSpec.class);

                    return by(
                        it("causes failure in multi-child describe nodes", () -> {
                            assertTrue(result.getFailureCount() > 0);
                            for (Failure failure : result.getFailures()) {
                                if (failure.getDescription().getMethodName().contains("broken multi-child describe")) {
                                    return;
                                }
                            }
                            fail();
                        }),
                        it("causes failure in single-child describe nodes", () -> {
                            assertTrue(result.getFailureCount() > 0);
                            for (Failure failure : result.getFailures()) {
                                if (failure.getDescription().getMethodName().contains("broken single-child describe")) {
                                    return;
                                }
                            }
                            fail();
                        }),
                        it("allows other statements in the same spec to proceed", () ->
                            assertTrue(result.getFailureCount() < result.getRunCount()))
                    );
                }),
                describe("root error", () -> {
                    Result result = new JUnitCore().run(ErrorInRootSpec.class);

                    return it("causes a single init failure for spec class", () -> {
                        assertEquals(1, result.getFailureCount());
                        Failure failure = result.getFailures().get(0);
                        String displayName = failure.getDescription().getDisplayName();
                        assertTrue(displayName.contains("initializationError"));
                        assertTrue(displayName.contains(ErrorInRootSpec.class.getName()));
                    });
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