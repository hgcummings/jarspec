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
        return leaf(this.description(), Optional.<Test>empty());
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

    /**
     * Create a new internal node in the tree representing a specification.
     * Consumers should generally use the default methods on {@link Specification}
     * instead, unless creating their own behaviour mixin.
     * @param description description for this node
     * @param children list of child nodes
     * @return the new node
     */
    public static SpecificationNode internal(String description, List<SpecificationNode> children) {
        return new Internal(description, immutableCopyOf(children), false);
    }

    /**
     * Create a new leaf node in the tree representing a specification.
     * Consumers should generally use the default methods on {@link Specification}
     * instead, unless creating their own behaviour mixin.
     * @param description description for this node
     * @param test optional automated test for this node
     * @return the new node
     */
    public static SpecificationNode leaf(String description, Optional<Test> test) {
        return new Leaf(description, test, false);
    }

    /**
     * Create a new error node in the tree representing a specification, indicating
     * an exception thrown while building the specification tree itself.
     * Consumers should generally use the default methods on {@link Specification}
     * instead, unless creating their own behaviour mixin.
     * @param description description for this node
     * @param throwable the original error
     * @return the new node
     */
    public static SpecificationNode error(String description, Throwable throwable) {
        return leaf(description, Optional.of(() -> {
            throw exceptionFrom(throwable);
        }));
    }

    /**
     * Represents the top-level node for a unit of behaviour. Objects of this
     * type are immutable iff the provided list of children is immutable.
     */
    private static class Internal extends SpecificationNode {
        private final String unit;
        private final List<SpecificationNode> children;

        private Internal(String unit, List<SpecificationNode> children, boolean solo) {
            super(solo);
            this.unit = unit;
            this.children = children;
        }

        @Override
        public SpecificationNode only() {
            return new Internal(unit, children, true);
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
    private static class Leaf extends SpecificationNode {
        private final String behaviour;
        private final Optional<Test> test;

        private Leaf(String behaviour, Optional<Test> test, boolean solo) {
            super(solo);
            this.behaviour = behaviour;
            this.test = test;
        }

        @Override
        public SpecificationNode only() {
            return new Leaf(behaviour, test, true);
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