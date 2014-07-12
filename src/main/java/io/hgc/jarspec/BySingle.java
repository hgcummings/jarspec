package io.hgc.jarspec;

/**
 * Represents a single sub-specification that belongs to another specification
 */
@FunctionalInterface
public interface BySingle {
    /**
     * @return a single nested Specification
     */
    Specification get() throws Exception;
}