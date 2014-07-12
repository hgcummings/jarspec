package io.hgc.jarspec.examples;

import io.hgc.jarspec.UnitSpec;
import io.hgc.jarspec.mixins.ExceptionBehaviour;
import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class ExceptionSpec implements UnitSpec, ExceptionBehaviour {
    @Override
    public Specification specification() {
        return describe("unstable code", () -> by(
            itThrows(ExpectedException.class, "on matching invocation", () -> {
                throw new ExpectedException();
            }),
            itThrows(ExpectedException.class, "on non-matching invocation", () -> {
                throw new OtherException();
            }),
            itThrows(ExpectedException.class, "on non-exception invocation", () -> {
                // Don't throw
            })
        ));
    }

    public class ExpectedException extends Exception {
    }

    public class OtherException extends Exception {
    }
}
