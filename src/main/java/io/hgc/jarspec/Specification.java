package io.hgc.jarspec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Specification consisting of statements about behaviour and automated tests
 * for those statements. Primary interface for all specs written with JarSpec.
 */
public interface Specification {
    public SpecificationNode root();

    /**
     * @param unit description of a unit of behaviour
     * @param specification nested specification for the behaviour of the unit
     * @return the overall specification
     */
    default public SpecificationNode describe(String unit, BySingle specification) {
        try {
            return new SpecificationNode.Aggregate(unit, by(specification.get()));
        } catch (Throwable e) {
            return new SpecificationNode.SpecError(unit, e);
        }
    }

    /**
     * @param unit description of a unit of behaviour
     * @param specifications nested specifications for the behaviour of the unit
     * @return the overall specification
     */
    default public SpecificationNode describe(String unit, ByMultiple specifications) {
        try {
            return new SpecificationNode.Aggregate(unit, specifications.get());
        } catch (Throwable e) {
            return new SpecificationNode.SpecError(unit, e);
        }
    }

    /**
     * @param statement a statement about the behaviour of a unit
     * @param test an automated test for the statement
     * @return a specification consisting of single automatically verifiable statement
     */
    default public SpecificationNode it(String statement, Test test) {
        return new SpecificationNode.Statement(statement, Optional.of(test));
    }

    /**
     * @param statement a statement about the behaviour of a unit
     * @return a specification consisting of single statement with no automated test
     */
    default public SpecificationNode it(String statement) {
        return new SpecificationNode.Statement(statement, Optional.<Test>empty());
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