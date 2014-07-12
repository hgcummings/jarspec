package io.hgc.jarspec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public abstract class Specification {
    private Specification() {}

    protected abstract Collection<Runnable> tests();

    protected abstract String description();

    protected abstract Specification[] children();

    public static Specification describe(String description, Supplier<Specification> test) {
        return new Node(description, new Specification[]{test.get()});
    }

    public static Specification describes(String description, Supplier<Specification[]> tests) {
        return new Node(description, tests.get());
    }

    public static Specification it(String description, Runnable test) {
        return new Leaf(description, test);
    }

    protected static class Node extends Specification {
        private String description;
        private Specification[] children;

        public Node(String description, Specification[] children) {
            this.description = description;
            this.children = children;
        }

        @Override
        protected Collection<Runnable> tests() {
            Collection<Runnable> tests = new ArrayList<>();
            for (Specification child : children) {
                tests.addAll(child.tests());
            }
            return tests;
        }

        @Override
        protected String description() {
            return description;
        }

        @Override
        protected Specification[] children() {
            return children;
        }
    }

    protected static class Leaf extends Specification {
        private String description;
        private Runnable test;

        public Leaf(String description, Runnable test) {
            this.description = description;
            this.test = test;
        }

        @Override
        protected Collection<Runnable> tests() {
            Collection<Runnable> tests = new ArrayList<>();
            tests.add(test);
            return tests;
        }

        @Override
        protected String description() {
            return description;
        }

        @Override
        protected Specification[] children() {
            return new Specification[0];
        }
    }
}
