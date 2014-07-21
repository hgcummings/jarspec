package io.hgc.jarspec.mixins;

import io.hgc.jarspec.SpecificationNode;
import java.util.Optional;

public interface DependencyBehaviour<TDependency> {

    default public SpecificationNode it(String statement, DependentTest<TDependency> test) {
        return SpecificationNode.leaf(statement, Optional.of(() -> {
            TDependency dependency = setupDependency();
            test.run(dependency);
            teardownDependency(dependency);
        }));
    }

    public TDependency setupDependency();

    default public void teardownDependency(TDependency dependency) { }
}

