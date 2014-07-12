package io.hgc.jarspec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Primary interface for all specs written with JarSpec, providing mixin helper methods for
 * building a {@link Specification}, which are in fact the only public methods for doing so
 */
public interface UnitSpec {
    public Specification specification();

    /**
     * @param unit a description for a unit of behaviour
     * @param specification sub-specification for the behaviour of the unit
     * @return the overall specification
     */
    default public Specification describe(String unit, BySingle specification) {
        try {
            return new Specification.Aggregate(unit, by(specification.get()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param unit a description for a unit of behaviour
     * @param specifications sub-specifications for the behaviour of the unit
     * @return the overall specification
     */
    default public Specification describe(String unit, ByMultiple specifications) {
        try {
            return new Specification.Aggregate(unit, specifications.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param statement a single statement about the behaviour of a unit
     * @param test automated test for the statement
     * @return a specification consisting of single automatically verifiable statement
     */
    default public Specification it(String statement, Test test) {
        return new Specification.Single(statement, test);
    }

    /**
     * Convenience method providing a concise syntax for combining specifications into a list
     * @param specifications specifications to be combined
     * @return a List containing all of the specifications in the order provided
     */
    default public List<Specification> by(Specification... specifications) {
        List<Specification> list = new ArrayList<>();
        Collections.addAll(list, specifications);
        return list;
    }
}