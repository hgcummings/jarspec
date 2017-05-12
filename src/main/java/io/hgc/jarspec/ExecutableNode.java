package io.hgc.jarspec;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * Internal class. Adapts a {@link SpecificationNode} into a form that can be executed by JUnit.
 */
class ExecutableNode {
    private SpecificationNode specificationNode;
    private Description description;
    private int priority;
    private int maxDescendantPriority;
    private List<ExecutableNode> children;

    ExecutableNode(SpecificationNode specificationNode) {
        this.specificationNode = specificationNode;
        children = new ArrayList<>();
        if (specificationNode.isSolo()) {
            setPriority(1);
        }
    }

    Description buildDescription() {
        description = Description.createSuiteDescription(specificationNode.description());
        specificationNode.children().forEachOrdered(child -> addChild(new ExecutableNode(child)));
        return description;
    }

    void execute(RunNotifier notifier) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        execute(threadPool, notifier, this.maxDescendantPriority, RuleChain.emptyRuleChain());
        try {
            threadPool.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPriority(int newPriority) {
        priority = newPriority;
        maxDescendantPriority = Math.max(priority, maxDescendantPriority);
    }

    private void addChild(ExecutableNode child) {
        child.setPriority(Math.max(child.priority, priority));
        description.addChild(child.buildDescription());
        children.add(child);
        maxDescendantPriority = Math.max(maxDescendantPriority, child.maxDescendantPriority);
    }

    private void execute(ExecutorService threadPool, RunNotifier notifier, int minPriority, RuleChain ruleChain) {
        if (hasOwnTest(minPriority)) {
            notifier.fireTestStarted(description);
        } else if (children.isEmpty()) {
            notifier.fireTestIgnored(description);
            return;
        }
        evaluateStatementAndNotifyFailures(
                withRules(RuleChain.emptyRuleChain(), this.specificationNode.blockRules()),
                executionStatement(threadPool, notifier, minPriority, ruleChain),
                notifier);
        if (hasOwnTest(minPriority)) {
            notifier.fireTestFinished(description);
        }
    }

    private Statement executionStatement(ExecutorService threadPool, final RunNotifier notifier, final int minPriority, final RuleChain ruleChain) {
        return new Statement() {
            @Override
            public void evaluate() {
                RuleChain ongoingRuleChain = withRules(ruleChain, specificationNode.rules());
                if (hasOwnTest(minPriority)) {
                    evaluateStatementAndNotifyFailures(
                        ongoingRuleChain,
                        specificationNode.test().get().asStatement(),
                        notifier);
                }
                for(ExecutableNode child : children) {
//                    threadPool.submit(() -> child.execute(threadPool, notifier, minPriority, ongoingRuleChain));
                    child.execute(threadPool, notifier, minPriority, ongoingRuleChain);
                }
            }
        };
    }

    private boolean hasOwnTest(int minPriority) {
        return specificationNode.test().isPresent() && priority >= minPriority;
    }

    private void evaluateStatementAndNotifyFailures(RuleChain ruleChain, Statement statement, RunNotifier notifier) {
        try {
            ruleChain.apply(statement, description).evaluate();
        } catch(AssumptionViolatedException e) {
            notifier.fireTestAssumptionFailed(new Failure(description, e));
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(description, e));
        }
    }

    private RuleChain withRules(RuleChain ruleChain, Stream<TestRule> rules) {
        // We really just want foldLeft here, but the closest equivalent in the Java 8 Streams API is reduce, which
        // doesn't allow us to assume the stream is sequential, so we implement foldLeft using forEachOrdered.
        // See http://stackoverflow.com/questions/29210176/can-a-collectors-combiner-function-ever-be-used-on-sequential-streams
        final AtomicReference<RuleChain> accumulator = new AtomicReference<>(ruleChain);
        rules.forEachOrdered(rule ->
                accumulator.getAndUpdate(acc -> acc.around(rule))
        );
        return accumulator.get();
    }
}
