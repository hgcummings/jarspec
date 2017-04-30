package io.hgc.jarspec;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Test runner for executing JarSpec specifications under JUnit
 *
 * @param <T> Specification class
 */
public class JarSpecJUnitRunner<T extends Specification> extends Runner {
    private final Class<T> specClass;
    private final Map<Description, Optional<Test>> allTests;
    private final Set<Description> soloTests;
    private final Description topLevelDescription;

    public JarSpecJUnitRunner(Class<T> specClass) throws InstantiationException {
        this.specClass = specClass;
        try {
            allTests = new LinkedHashMap<>();
            soloTests = new HashSet<>();
            topLevelDescription = visitTree(specClass.newInstance().root());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Description getDescription() {
        return topLevelDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        allTests.forEach((description, test) -> {
            final boolean included = soloTests.isEmpty() || soloTests.contains(description);
            if (included && test.isPresent()) {
                runTest(description, test.get(), notifier);
            } else {
                notifier.fireTestIgnored(description);
            }
        });
    }

    private Description visitTree(SpecificationNode specificationNode) {
        final Description description = Description.createTestDescription(specClass, specificationNode.description());
        visitTree(specificationNode, "", specificationNode.isSolo()).forEach(description::addChild);
        return description;
    }

    private List<Description> visitTree(SpecificationNode specificationNode, String prefix, boolean soloParent) {
        final List<Description> descriptions = new ArrayList<>();
        final String text = prefix + specificationNode.description();
        final Description description = Description.createTestDescription(specClass, text);
        final boolean isSolo = soloParent || specificationNode.isSolo();

        if (specificationNode.test().isPresent()) {
            descriptions.add(description);
            allTests.put(description, specificationNode.test());
            if (isSolo) {
                soloTests.add(description);
            }
        }

        specificationNode.children().forEach(child ->
            descriptions.addAll(visitTree(child, text + " ", isSolo))
        );

        if (descriptions.isEmpty()) { // Neither has a test nor has children, but still want to include it as skipped
            descriptions.add(description);
            allTests.put(description, specificationNode.test());
        }

        return descriptions;
    }

    private void runTest(Description description, Test test, RunNotifier notifier) {
        try {
            notifier.fireTestStarted(description);
            test.run();
        } catch(AssumptionViolatedException e) {
            notifier.fireTestAssumptionFailed(new Failure(description, e));
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(description, e));
        }
        notifier.fireTestFinished(description);
    }
}