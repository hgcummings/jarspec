package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assume.assumeTrue;

@RunWith(JarSpecJUnitRunner.class)
public class AssumptionFailureSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return it("assumes the worse", () -> assumeTrue(false));
    }
}
