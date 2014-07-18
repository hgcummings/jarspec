package io.hgc.jarspec;

import java.util.*;

/**
 * Internal implementation of {@link Specification} as a tree structure.
 */
public abstract class SpecificationNode {
    private SpecificationNode() {}

    public SpecificationNode skip() {
        return new Statement(this.description(), Optional.<Test>empty());
    }

    abstract Optional<Test> test();

    abstract String description();

    abstract List<SpecificationNode> children();

    static class Aggregate extends SpecificationNode {
        private final String unit;
        private final List<SpecificationNode> children;

        public Aggregate(String unit, List<SpecificationNode> children) {
            this.unit = unit;
            this.children = children;
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

    static class Statement extends SpecificationNode {
        private final String behaviour;
        private final Optional<Test> test;

        public Statement(String behaviour, Optional<Test> test) {
            this.behaviour = behaviour;
            this.test = test;
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

    static class SpecError extends SpecificationNode {
        private final String unit;
        private final Exception exception;

        public SpecError(String unit, Throwable throwable) {
            this.unit = unit;
            if (throwable instanceof Exception) {
                this.exception = (Exception) throwable;
            } else {
                this.exception = new RuntimeException(throwable);
            }
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
    }
}