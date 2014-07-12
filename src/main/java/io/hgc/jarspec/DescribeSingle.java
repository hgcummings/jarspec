package io.hgc.jarspec;

@FunctionalInterface
public interface DescribeSingle {
    Specification get() throws Exception;
}
