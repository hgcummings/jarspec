package io.hgc.jarspec;

import java.util.stream.Stream;

/**
 * Returns multiple {@link SpecificationNode} that represent the direct children of another
 */
@FunctionalInterface
public interface ByMultiple {
    /**
     * @return multiple {@link SpecificationNode} children
     * @throws Exception indicates a test failure
     */
    Stream<SpecificationNode> get() throws Exception;
}