package io.hgc.jarspec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return leaf(this.description(), Optional.empty());
    }

    /**
     * Mark this node to be run exclusively (along with its children and any other nodes similarly marked)
     * @return a new node representing an exclusive version of the original node
     */
    public abstract SpecificationNode only();

    /**
     * Define a reset operation to clean up shared state between tests
     * @param reset the reset operation
     * @return a new version of this node that will reset state before each test
     */
    public abstract SpecificationNode withReset(ResetSharedState reset);

    abstract String description();

    abstract Optional<Test> test();

    /**
     * TODO: Make this a more general type (e.g. Iterator or Stream)
     * @return Child nodes of this node
     */
    abstract List<SpecificationNode> children();

    boolean isSolo() {
        return solo;
    }

    /**
     * Factory method for internal nodes in the tree representing a specification.
     * Consumers should generally use the default methods on {@link Specification}
     * instead, unless creating their own behaviour mixin.
     * @param description description for this node
     * @param children list of child nodes
     * @return a newly-created node
     */
    public static SpecificationNode internal(String description, List<SpecificationNode> children) {
        return new Internal(description, immutableCopyOf(children), false);
    }

    /**
     * Factory method for leaf nodes in the tree representing a specification.
     * Consumers should generally use the default methods on {@link Specification}
     * instead, unless creating their own behaviour mixin.
     * @param description description for this node
     * @param test optional automated test for this node
     * @return a newly-created node
     */
    public static SpecificationNode leaf(String description, Optional<Test> test) {
        return new Leaf(description, test, false);
    }

    /**
     * Factory method for error nodes in the tree representing a specification,
     * indicating an exception thrown while building the specification tree itself.
     * Consumers should generally use the default methods on {@link Specification}
     * instead, unless creating their own behaviour mixin.
     * @param description description for this node
     * @param throwable the original error
     * @return a newly-created node
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
        public SpecificationNode withReset(ResetSharedState reset) {
            return new SpecificationNode.Internal(
                    this.unit,
                    this.children.stream().map(child -> child.withReset(reset))
            .collect(Collectors.toList()), this.isSolo());
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
        public SpecificationNode withReset(ResetSharedState reset) {
            if (this.test.isPresent()) {
                return new Leaf(this.behaviour, Optional.of(() -> {
                    reset.reset();
                    test.get().run();
                }), this.isSolo());
            } else {
                return this;
            }
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

    @FunctionalInterface
    public interface ResetSharedState {
        void reset();
    }
}