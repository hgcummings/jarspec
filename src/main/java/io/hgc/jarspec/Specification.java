package io.hgc.jarspec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Specification {
    private Specification() {}

    protected abstract Optional<Runnable> test();

    protected abstract String description();

    protected abstract Specification[] children();

    public abstract void visitSpecifications(Consumer<Specification> visitor);

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
        protected Optional<Runnable> test() {
            return Optional.empty();
        }

        @Override
        protected String description() {
            return description;
        }

        @Override
        protected Specification[] children() {
            return children;
        }

        @Override
        public void visitSpecifications(Consumer<Specification> visitor) {
            visitor.accept(this);
            for (Specification child : children) {
                visitor.accept(child);
            }
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
        protected Optional<Runnable> test() {
            return Optional.of(test);
        }

        @Override
        protected String description() {
            return description;
        }

        @Override
        protected Specification[] children() {
            return new Specification[0];
        }

        @Override
        public void visitSpecifications(Consumer<Specification> visitor) {
            visitor.accept(this);
        }
    }
}
