package io.hgc.jarspec;

import java.util.List;

@FunctionalInterface
public interface DescribeMultiple {
    List<Specification> get();
}
