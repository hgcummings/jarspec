package io.hgc.jarspec;

import java.util.*;

public abstract class Specification {
    private Specification() {}

    protected abstract Optional<Test> test();

    protected abstract String statement();

    protected abstract List<Specification> children();

    protected static class Aggregate extends Specification {
        private String description;
        private List<Specification> children;

        public Aggregate(String description, List<Specification> children) {
            this.description = description;
            this.children = children;
        }

        @Override
        protected Optional<Test> test() {
            return Optional.empty();
        }

        @Override
        protected String statement() {
            return description;
        }

        @Override
        protected List<Specification> children() {
            return children;
        }
    }

    protected static class Single extends Specification {
        private String statement;
        private Test test;

        public Single(String statement, Test test) {
            this.statement = statement;
            this.test = test;
        }

        @Override
        protected Optional<Test> test() {
            return Optional.of(test);
        }

        @Override
        protected String statement() {
            return statement;
        }

        @Override
        protected List<Specification> children() {
            return Collections.emptyList();
        }
    }
}