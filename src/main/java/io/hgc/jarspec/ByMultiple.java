package io.hgc.jarspec;

import java.util.List;

@FunctionalInterface
public interface ByMultiple {
    List<Specification> get() throws Exception;
}
