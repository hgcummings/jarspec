package io.hgc.jarspec;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import sun.security.krb5.internal.crypto.Des;

import java.util.function.Supplier;

public class JarSpecJUnitRunner<T extends Supplier<Specification>> extends Runner {
    private T testInstance;

    public JarSpecJUnitRunner(Class<T> testClass) {
        try {
            testInstance = testClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Description getDescription() {
        return null;
    }

    @Override
    public void run(RunNotifier notifier) {

    }

    @Override
    public int testCount() {
        return testInstance.get().tests().size();
    }
}
