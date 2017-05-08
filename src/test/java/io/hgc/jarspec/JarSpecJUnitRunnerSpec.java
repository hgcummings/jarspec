package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.*;
import io.hgc.jarspec.mixins.ExceptionBehaviour;
import io.hgc.jarspec.mixins.TestRunnerBehaviour;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Uses {@link io.hgc.jarspec.fixtures} to test the main runner class
 */
@RunWith(JarSpecJUnitRunner.class)
public class JarSpecJUnitRunnerSpec implements Specification, ExceptionBehaviour, TestRunnerBehaviour {
    @Override
    public SpecificationNode root() {
        return describe("JUnit runner", () -> {
            Runner runner = new JarSpecJUnitRunner<>(AdditionSpec.class);
            return byAllOf(
                    describe("constructor",
                            itThrows(RuntimeException.class, "for inaccessible test class", () -> {
                                new JarSpecJUnitRunner<>(InaccessibleTestClass.class);
                                fail("This line should not be reached");
                            })
                    ),
                    describe("test count",
                            it("matches number of tests", () -> assertEquals(2, runner.testCount()))
                    ),
                    describe("description",
                        it("has class description as the root node", () -> {
                            Description description = runner.getDescription();
                            assertEquals(AdditionSpec.class.getName(), description.getClassName());
                        }),
                        it("reflects the structure of the spec", () -> {
                            Description classDescription = runner.getDescription();
                            assertEquals(1, classDescription.getChildren().size());

                            Description additionDescription = classDescription.getChildren().get(0);
                            assertEquals(1, additionDescription.getChildren().size());

                            Description ofOnePlusOneDescription = additionDescription.getChildren().get(0);
                            assertEquals(2, ofOnePlusOneDescription.getChildren().size());
                        })
                    ),
                    it("reports failures correctly", () -> {
                        JUnitCore jUnitCore = new JUnitCore();
                        Result result = jUnitCore.run(runner);
                        assertEquals(1, result.getFailureCount());
                        Failure failure = result.getFailures().get(0);
                        assertEquals("equals 3", failure.getDescription().getDisplayName());
                    }),
                    it("does not report assumption failures as test failures", () -> {
                        Result result = new JUnitCore().run(AssumptionFailureSpec.class);
                        assertEquals(0, result.getFailureCount());
                    }),
                    it("reports skipped tests as ignored", () -> {
                        Result result = new JUnitCore().run(SkippedTestSpec.class);
                        assertEquals(0, result.getFailureCount());
                        assertEquals(3, result.getIgnoreCount());
                    }),
                    it("runs only selected tests when any tests are selected", () -> {
                        Result result = new JUnitCore().run(SelectiveTestExecutionSpec.class);
                        assertEquals(1, result.getFailureCount());
                        assertEquals(3, result.getIgnoreCount());
                        assertTrue(result.getFailures().get(0).getDescription().getDisplayName().contains("selected"));
                    }),
                    it("runs all tests in selected units", () -> {
                        Result result = new JUnitCore().run(SelectiveDescribeExecutionSpec.class);
                        assertEquals(0, result.getFailureCount());
                        assertEquals(2, result.getIgnoreCount());
                    }),
                    describe("error handling", by(ErrorInDescribeSpec.class,
                        fails("broken multi-child unit"),
                        fails("broken single-child unit"),
                        fails("unit with unchecked Error"),
                        passes("successful unit"))
                    ),
                    describe("root error",
                        it("causes a single init failure for spec class", () -> {
                            Result result = new JUnitCore().run(ErrorInRootSpec.class);
                            assertEquals(1, result.getFailureCount());
                            Failure failure = result.getFailures().get(0);
                            String displayName = failure.getDescription().getDisplayName();
                            assertTrue(displayName.contains("initializationError"));
                            assertTrue(displayName.contains(ErrorInRootSpec.class.getName()));
                        }))
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