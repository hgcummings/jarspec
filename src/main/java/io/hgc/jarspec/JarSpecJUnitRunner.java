package io.hgc.jarspec;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

/**
 * Test runner for executing JarSpec specifications under JUnit.
 *
 * @param <T> Specification class
 */
public class JarSpecJUnitRunner<T extends Specification> extends Runner {
    private final ExecutableNode rootExecutableNode;
    private final Description classDescription;

    public JarSpecJUnitRunner(Class<T> specClass) throws InstantiationException {
        try {
            classDescription = Description.createSuiteDescription(specClass);
            rootExecutableNode = new ExecutableNode(specClass.newInstance().root());
            classDescription.addChild(rootExecutableNode.buildDescription());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Description getDescription() {
        return classDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        rootExecutableNode.execute(notifier);
    }
}