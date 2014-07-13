package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

@RunWith(JarSpecJUnitRunner.class)
public class ErrorInRootSpec implements Specification {
    @Override
    public SpecificationNode root() {
        throw new RuntimeException("Test exception thrown before SpecificationNode returned");
    }
}
