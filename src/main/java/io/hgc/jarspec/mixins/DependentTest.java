package io.hgc.jarspec.mixins;

@FunctionalInterface
public interface DependentTest<TDependency> {
    void run(TDependency dependency) throws Exception;
}
