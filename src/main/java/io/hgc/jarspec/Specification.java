package io.hgc.jarspec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Specification consisting of statements about behaviour and automated tests
 * for those statements. Primary interface for all specs written with JarSpec.
 */
public interface Specification {
    public SpecificationNode root();

    /**
     * @param unit a description for a unit of behaviour
     * @param specification sub-root for the behaviour of the unit
     * @return the root of the overall specification
     */
    default public SpecificationNode describe(String unit, BySingle specification) {
        try {
            return new SpecificationNode.Aggregate(unit, by(specification.get()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param unit a description for a unit of behaviour
     * @param specifications sub-specifications for the behaviour of the unit
     * @return the overall root
     */
    default public SpecificationNode describe(String unit, ByMultiple specifications) {
        try {
            return new SpecificationNode.Aggregate(unit, specifications.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param statement a single description about the behaviour of a unit
     * @param test automated test for the description
     * @return a root consisting of single automatically verifiable description
     */
    default public SpecificationNode it(String statement, Test test) {
        return new SpecificationNode.Statement(statement, test);
    }

    /**
     * Convenience method providing a concise syntax for combining specifications into a list
     * @param specificationNodes specifications to be combined
     * @return a List containing all of the specifications in the order provided
     */
    default public List<SpecificationNode> by(SpecificationNode... specificationNodes) {
        List<SpecificationNode> list = new ArrayList<>();
        Collections.addAll(list, specificationNodes);
        return list;
    }
}