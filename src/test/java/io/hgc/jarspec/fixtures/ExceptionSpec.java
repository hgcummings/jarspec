package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import io.hgc.jarspec.mixins.ExceptionBehaviour;
import io.hgc.jarspec.JarSpecJUnitRunner;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class ExceptionSpec implements Specification, ExceptionBehaviour {
    @Override
    public SpecificationNode root() {
        return describe("unstable code",
            itThrows(ExpectedException.class, "matching exception", () -> {
                throw new ExpectedException();
            }),
            itThrows(ExpectedException.class, "non-matching exception", () -> {
                throw new OtherException();
            }),
            itThrows(ExpectedException.class, "no exception", () -> {
                // Don't throw
            })
        );
    }

    public class ExpectedException extends Exception {
    }

    public class OtherException extends Exception {
    }
}
