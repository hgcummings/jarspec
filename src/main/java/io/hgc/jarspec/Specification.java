package io.hgc.jarspec;

import java.util.*;

public abstract class Specification {
    private Specification() {}

    protected abstract Optional<Test> test();

    protected abstract String description();

    protected abstract List<Specification> children();

    public static Specification describe(String description, DescribeSingle test) {
        try {
            return new Node(description, all(test.get()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Specification describe(String description, DescribeMultiple tests) {
        try {
            return new Node(description, tests.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Specification it(String description, Test test) {
        return new Leaf(description, test);
    }

    @SafeVarargs
    public static<T> List<T> all(T... elems) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, elems);
        return list;
    }

    protected static class Node extends Specification {
        private String description;
        private List<Specification> children;

        public Node(String description, List<Specification> children) {
            this.description = description;
            this.children = children;
        }

        @Override
        protected Optional<Test> test() {
            return Optional.empty();
        }

        @Override
        protected String description() {
            return description;
        }

        @Override
        protected List<Specification> children() {
            return children;
        }
    }

    protected static class Leaf extends Specification {
        private String description;
        private Test test;

        public Leaf(String description, Test test) {
            this.description = description;
            this.test = test;
        }

        @Override
        protected Optional<Test> test() {
            return Optional.of(test);
        }

        @Override
        protected String description() {
            return description;
        }

        @Override
        protected List<Specification> children() {
            return Collections.EMPTY_LIST;
        }
    }
}
