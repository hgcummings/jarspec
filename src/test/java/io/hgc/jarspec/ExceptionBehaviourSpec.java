package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.ExceptionSpec;
import io.hgc.jarspec.mixins.ExceptionBehaviour;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.*;

@RunWith(JarSpecJUnitRunner.class)
public class ExceptionBehaviourSpec implements Specification, ExceptionBehaviour {
    @Override
    public SpecificationNode root() {
        Result result = new JUnitCore().run(ExceptionSpec.class);
        return describe("'itThrows' behaviour", () -> by(
            it("does not fail tests that throw an expected exception", () ->
                assertTrue(result.getFailureCount() < result.getRunCount())),
            it("fails tests that throw an unexpected exception", () -> {
                assertTrue(result.getFailureCount() > 0);
                for (Failure failure : result.getFailures()) {
                    if (failure.getDescription().getMethodName().contains("on non-matching invocation")) {
                        return;
                    }
                }
                fail();
            }),
            it("fails tests that do not throw an exception", () -> {
                assertTrue(result.getFailureCount() > 0);
                for (Failure failure : result.getFailures()) {
                    if (failure.getDescription().getMethodName().contains("on non-exception invocation")) {
                        return;
                    }
                }
                fail();
            })
        ));
    }
}
