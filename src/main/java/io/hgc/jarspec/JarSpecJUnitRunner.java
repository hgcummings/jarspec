package io.hgc.jarspec;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.util.Optional;
import java.util.function.Supplier;

public class JarSpecJUnitRunner<T extends Supplier<Specification>> extends Runner {
    private Class<T> testClass;
    private T testInstance;

    public JarSpecJUnitRunner(Class<T> testClass) {
        this.testClass = testClass;
        try {
            testInstance = testClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Description getDescription() {
        Specification specification = testInstance.get();
        return visitTree(specification, Optional.empty());
    }

    private Description visitTree(Specification specification, Optional<RunNotifier> notifier) {
        return visitTree(testInstance.get(), notifier, "");
    }

    private Description visitTree(Specification specification, Optional<RunNotifier> notifier, String prefix) {
        String text = prefix + specification.description();
        Description description = Description.createTestDescription(testClass, text);
        if (notifier.isPresent()) {
            runTest(specification.test(), notifier.get(), description);
        }
        if (specification.children().length > 0) {
            for (Specification child : specification.children()) {
                description.addChild(visitTree(child, notifier, text + " "));
            }
        }
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        visitTree(testInstance.get(), Optional.of(notifier));
    }

    private void runTest(Optional<Runnable> test, RunNotifier notifier, Description description) {
        if (test.isPresent()) {
            try {
                notifier.fireTestStarted(description);
                test.get().run();
            } catch (Throwable e) {
                notifier.fireTestFailure(new Failure(description, e));
            } finally {
                notifier.fireTestFinished(description);
            }
        }
    }
}
