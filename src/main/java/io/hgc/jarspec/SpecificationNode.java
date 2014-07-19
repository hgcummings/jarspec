package io.hgc.jarspec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.hgc.jarspec.Util.*;

/**
 * Internal implementation of {@link Specification} as a tree structure.
 */
public abstract class SpecificationNode {
    private final boolean solo;

    private SpecificationNode(boolean solo) {
        this.solo = solo;
    }

    /**
     * Mark this node to be skipped (along with its children and any other nodes similarly marked)
     * @return a new node representing a skipped version of the original node
     */
    public SpecificationNode skip() {
        return statement(this.description(), Optional.<Test>empty());
    }

    /**
     * Mark this node to be run exclusively (along with its children and any other nodes similarly marked)
     * @return a new node representing an exclusive version of the original node
     */
    public abstract SpecificationNode only();

    abstract String description();

    abstract Optional<Test> test();

    abstract List<SpecificationNode> children();

    boolean isSolo() {
        return solo;
    }

    static SpecificationNode describe(String unit, List<SpecificationNode> children) {
        return new Aggregate(unit, immutableCopyOf(children), false);
    }

    static SpecificationNode statement(String behaviour, Optional<Test> test) {
        return new Statement(behaviour, test, false);
    }

    static SpecificationNode error(String description, Throwable throwable) {
        return statement(description, Optional.of(() -> {
            throw exceptionFrom(throwable);
        }));
    }

    /**
     * Represents a unit of behaviour with children. Objects of this type
     * are immutable iff the provided list of children is immutable.
     */
    private static class Aggregate extends SpecificationNode {
        private final String unit;
        private final List<SpecificationNode> children;

        private Aggregate(String unit, List<SpecificationNode> children, boolean solo) {
            super(solo);
            this.unit = unit;
            this.children = children;
        }

        @Override
        public SpecificationNode only() {
            return new Aggregate(unit, children, true);
        }

        @Override
        Optional<Test> test() {
            return Optional.empty();
        }

        @Override
        String description() {
            return unit;
        }

        @Override
        List<SpecificationNode> children() {
            return children;
        }
    }

    /**
     * Represents a single statement, optionally with an automated test.
     * Objects of this type are immutable, although the test itself may
     * form a closure over immutable variables (in fact this is likely).
     */
    private static class Statement extends SpecificationNode {
        private final String behaviour;
        private final Optional<Test> test;

        private Statement(String behaviour, Optional<Test> test, boolean solo) {
            super(solo);
            this.behaviour = behaviour;
            this.test = test;
        }

        @Override
        public SpecificationNode only() {
            return new Statement(behaviour, test, true);
        }

        @Override
        Optional<Test> test() {
            return test;
        }

        @Override
        String description() {
            return behaviour;
        }

        @Override
        List<SpecificationNode> children() {
            return Collections.emptyList();
        }
    }
}