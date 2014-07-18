package io.hgc.jarspec;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.hgc.jarspec.Util.immutableCopy;

/**
 * Internal implementation of {@link Specification} as a tree structure.
 */
public abstract class SpecificationNode {
    private SpecificationNode() {}

    /**
     * Mark this node to be skipped (along with its children and any other nodes similarly marked)
     * @return a new node representing a skipped version of the original node
     */
    public SpecificationNode skip() {
        return new Statement(this.description(), Optional.<Test>empty());
    }

    /**
     * Mark this node to be run exclusively (along with its children and any other nodes similarly marked)
     * @return a new node representing an exclusive version of the original node
     */
    public abstract SpecificationNode only();

    abstract Optional<Test> test();

    abstract String description();

    abstract List<SpecificationNode> children();

    abstract boolean isSolo();

    /**
     * Represents a unit of behaviour with children.
     * Objects of this type are immutable.
     */
    static class Aggregate extends SpecificationNode {
        private final String unit;
        private final List<SpecificationNode> children;
        private final boolean solo;

        public Aggregate(String unit, List<SpecificationNode> children) {
            this(unit, immutableCopy(children), false);
        }

        private Aggregate(String unit, List<SpecificationNode> children, boolean solo) {
            this.unit = unit;
            this.children = children;
            this.solo = solo;
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

        @Override
        boolean isSolo() {
            return solo;
        }
    }

    /**
     * Represents a single statement, optionally with an automated test.
     * Objects of this type are immutable.
     */
    static class Statement extends SpecificationNode {
        private final String behaviour;
        private final Optional<Test> test;
        private final boolean solo;

        public Statement(String behaviour, Optional<Test> test) {
            this(behaviour, test, false);
        }

        private Statement(String behaviour, Optional<Test> test, boolean solo) {
            this.behaviour = behaviour;
            this.test = test;
            this.solo = solo;
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

        @Override
        boolean isSolo() {
            return solo;
        }
    }

    /**
     * Represents an error thrown during the creation of a specification (i.e.
     * from a describe block rather than a test).
     * Objects of this type are not purely immutable since they will contain
     * an Exception, and Throwables cannot be immutable. However, they will only
     * expose their Exception by actually throwing it.
     */
    static class SpecError extends SpecificationNode {
        private final String unit;
        private final Exception exception;
        private final boolean solo;

        public SpecError(String unit, Throwable throwable) {
            this(unit, throwable, false);
        }

        private SpecError(String unit, Throwable throwable, boolean solo) {
            this.unit = unit;
            if (throwable instanceof Exception) {
                this.exception = (Exception) throwable;
            } else {
                this.exception = new RuntimeException(throwable);
            }
            this.solo = solo;
        }

        @Override
        public SpecificationNode only() {
            return new SpecError(unit, exception, true);
        }

        @Override
        Optional<Test> test() {
            return Optional.of(() -> { throw exception; });
        }

        @Override
        String description() {
            return unit;
        }

        @Override
        List<SpecificationNode> children() {
            return Collections.emptyList();
        }

        @Override
        boolean isSolo() {
            return solo;
        }
    }
}