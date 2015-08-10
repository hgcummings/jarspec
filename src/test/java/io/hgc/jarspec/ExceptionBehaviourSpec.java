package io.hgc.jarspec;

import io.hgc.jarspec.fixtures.ExceptionSpec;
import io.hgc.jarspec.mixins.ExceptionBehaviour;
import io.hgc.jarspec.mixins.TestRunnerBehaviour;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class ExceptionBehaviourSpec implements Specification, ExceptionBehaviour, TestRunnerBehaviour {
    @Override
    public SpecificationNode root() {
        return describe("'itThrows' behaviour", by(ExceptionSpec.class,
            passes("matching exception"),
            fails("non-matching exception"),
            fails("no exception"))
        );
    }
}
