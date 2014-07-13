package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import io.hgc.jarspec.ExceptionBehaviour;
import io.hgc.jarspec.JarSpecJUnitRunner;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class ExceptionSpec implements Specification, ExceptionBehaviour {
    @Override
    public SpecificationNode root() {
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
