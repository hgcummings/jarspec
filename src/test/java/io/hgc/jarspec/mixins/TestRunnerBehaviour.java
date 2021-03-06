package io.hgc.jarspec.mixins;

import io.hgc.jarspec.ByMultiple;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public interface TestRunnerBehaviour extends Specification {

    default ByMultiple by(Class<? extends Specification> clazz, TestResult... expectedResults) {
        return () -> {
            Result result = new JUnitCore().run(clazz);
            return Stream.of(expectedResults)
                    .map(expectedResult -> it(expectedResult.statement(), expectedResult.test(result)));
        };
    }

    default TestResult passes(String testName) {
        return new PassingTestResult(testName);
    }

    default TestResult fails(String testName) {
        return new FailingTestResult(testName);
    }

    interface TestResult {
        String statement();
        Test test(Result result);
    }

    class PassingTestResult implements TestResult {
        private String testName;

        public PassingTestResult(String testName) {
            this.testName = testName;
        }

        public String statement() {
            return String.format("does not report %s as a failure", testName);
        }

        public Test test(Result result) {
            return () -> {
                assertTrue(result.getFailureCount() < result.getRunCount());
                for (Failure failure : result.getFailures()) {
                    if (failure.getDescription().getDisplayName().equals(testName)) {
                        fail();
                    }
                }
            };
        }
    }

    class FailingTestResult implements TestResult {
        private String testName;

        public FailingTestResult(String testName) {
            this.testName = testName;
        }

        public String statement() {
            return String.format("reports %s as a failure", testName);
        }

        public Test test(Result result) {
            return () -> {
                assertTrue(result.getFailureCount() > 0);
                for (Failure failure : result.getFailures()) {
                    if (failure.getDescription().getDisplayName().endsWith(testName)) {
                        return;
                    }
                }
                fail();
            };
        }
    }
}