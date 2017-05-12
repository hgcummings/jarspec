package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.*;
import io.hgc.jarspec.mixins.ExceptionBehaviour;
import io.hgc.jarspec.mixins.TestRunnerBehaviour;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;

import static org.assertj.core.api.Assertions.*;

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
                            it("matches number of tests", () -> assertThat(runner.testCount()).isEqualTo(2))
                    ),
                    describe("description",
                        it("has class description as the root node", () -> {
                            Description description = runner.getDescription();
                            assertThat(description.getClassName()).isEqualTo(AdditionSpec.class.getName());
                        }),
                        it("reflects the structure of the spec", () -> {
                            Description classDescription = runner.getDescription();
                            assertThat(classDescription.getChildren()).hasSize(1);

                            Description additionDescription = classDescription.getChildren().get(0);
                            assertThat(additionDescription.getChildren()).hasSize(1);

                            Description ofOnePlusOneDescription = additionDescription.getChildren().get(0);
                            assertThat(ofOnePlusOneDescription.getChildren()).hasSize(2);
                        })
                    ),
                    it("reports failures correctly", () -> {
                        JUnitCore jUnitCore = new JUnitCore();
                        Result result = jUnitCore.run(runner);
                        assertThat(result.getRunCount()).isEqualTo(2);
                        assertThat(result.getFailureCount()).isEqualTo(1);
                        Failure failure = result.getFailures().get(0);
                        assertThat(failure.getDescription().getDisplayName()).isEqualTo("equals 3");
                    }),
                    it("does not report assumption failures as test failures", () -> {
                        Result result = new JUnitCore().run(AssumptionFailureSpec.class);
                        assertThat(result.getFailureCount()).isEqualTo(0);
                    }),
                    it("reports skipped tests as ignored", () -> {
                        Result result = new JUnitCore().run(SkippedTestSpec.class);
                        assertThat(result.getFailureCount()).isEqualTo(0);
                        assertThat(result.getIgnoreCount()).isEqualTo(3);
                    }),
                    it("runs only selected tests when any tests are selected", () -> {
                        Result result = new JUnitCore().run(SelectiveTestExecutionSpec.class);
                        assertThat(result.getFailureCount()).isEqualTo(1);
                        assertThat(result.getIgnoreCount()).isEqualTo(3);
                        assertThat(result.getRunCount()).isEqualTo(2);
                        assertThat(result.getFailures().get(0).getDescription().getDisplayName().contains("selected")).isTrue();
                    }),
                    it("runs all tests in selected units", () -> {
                        Result result = new JUnitCore().run(SelectiveDescribeExecutionSpec.class);
                        assertThat(result.getFailureCount()).isEqualTo(0);
                        assertThat(result.getIgnoreCount()).isEqualTo(2);
                        assertThat(result.getRunCount()).isEqualTo(3);
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
                            assertThat(result.getFailureCount()).isEqualTo(1);
                            Failure failure = result.getFailures().get(0);
                            String displayName = failure.getDescription().getDisplayName();
                            assertThat(displayName.contains("initializationError")).isTrue();
                            assertThat(displayName.contains(ErrorInRootSpec.class.getName())).isTrue();
                        }))
                    );
        });
    }

    public static class InaccessibleTestClass implements Specification {
        private InaccessibleTestClass() {
        }

        @Override
        public SpecificationNode root() {
            return it("should be true", () -> assertThat(true).isTrue());
        }
    }
}