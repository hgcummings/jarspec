package io.hgc.jarspec;

import org.junit.runner.Description;
import org.junit.runner.Runner;
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
        return createDescriptionFromSpecification(specification);
    }

    private Description createDescriptionFromSpecification(Specification specification) {
        Description description = Description.createTestDescription(testClass, specification.description());
        if (specification.children().length > 0) {
            for (Specification child : specification.children()) {
                description.addChild(createDescriptionFromSpecification(child));
            }
        }
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {

    }
}
