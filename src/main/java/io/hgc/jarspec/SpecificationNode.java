package io.hgc.jarspec;

import java.util.*;

/**
 * Internal implementation of {@link Specification} as a tree structure.
 */
public abstract class SpecificationNode {
    private SpecificationNode() {}

    abstract Optional<Test> test();

    abstract String description();

    abstract List<SpecificationNode> children();

    static class Aggregate extends SpecificationNode {
        private String description;
        private List<SpecificationNode> children;

        public Aggregate(String description, List<SpecificationNode> children) {
            this.description = description;
            this.children = children;
        }

        @Override
        Optional<Test> test() {
            return Optional.empty();
        }

        @Override
        String description() {
            return description;
        }

        @Override
        List<SpecificationNode> children() {
            return children;
        }
    }

    static class Statement extends SpecificationNode {
        private String behaviour;
        private Test test;

        public Statement(String behaviour, Test test) {
            this.behaviour = behaviour;
            this.test = test;
        }

        @Override
        Optional<Test> test() {
            return Optional.of(test);
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