package io.hgc.jarspec;

/**
 * Returns a single {@link SpecificationNode} that represents the only direct child of another
 */
@FunctionalInterface
public interface BySingle {
    /**
     * @return the {@link SpecificationNode} child
     * @throws Exception
     */
    SpecificationNode get() throws Exception;
}