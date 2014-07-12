package io.hgc.jarspec;

import java.util.List;

/**
 * Returns multiple {@link SpecificationNode} that represent the direct children of another
 */
@FunctionalInterface
public interface ByMultiple {
    /**
     * @return multiple {@link SpecificationNode} children
     * @throws Exception indicates a test failure
     */
    List<SpecificationNode> get() throws Exception;
}