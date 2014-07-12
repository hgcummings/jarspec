package io.hgc.jarspec;

import java.util.List;

/**
 * Represents multiple sub-specifications that make up a single specification
 */
@FunctionalInterface
public interface ByMultiple {
    /**
     * @return multiple nested Specifications
     * @throws Exception
     */
    List<Specification> get() throws Exception;
}