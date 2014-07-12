package io.hgc.jarspec;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import sun.security.krb5.internal.crypto.Des;

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
        return createDescriptionTree(specification);
    }

    private Description createDescriptionTree(Specification specification) {
        Description description = createDescriptionNode(specification);
        if (specification.children().length > 0) {
            for (Specification child : specification.children()) {
                description.addChild(createDescriptionTree(child));
            }
        }
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        Specification specification = testInstance.get();
        run(notifier, specification);
    }

    private void run(RunNotifier notifier, Specification specification) {
        if (specification.children().length > 0) {
            for (Specification child : specification.children()) {
                if (child.test().isPresent()) {
                    Description description = createDescriptionNode(child);
                    try {
                        notifier.fireTestStarted(description);
                        child.test().get().run();
                    } catch (Throwable e) {
                        notifier.fireTestFailure(new Failure(description, e));
                    } finally {
                        notifier.fireTestFinished(description);
                    }
                }

                run(notifier, child);
            }
        }
    }

    private Description createDescriptionNode(Specification specification) {
        return Description.createTestDescription(testClass, specification.description());
    }
}
