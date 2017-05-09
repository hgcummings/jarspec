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
        specificationNode.children().forEach(child -> {
            ExecutableNode childExecutionNode = new ExecutableNode(child);
            addChild(childExecutionNode);
        });
        return description;
    }

    void execute(RunNotifier notifier) {
        execute(notifier, this.maxDescendantPriority, RuleChain.emptyRuleChain());
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

    private void execute(RunNotifier notifier, int minPriority, RuleChain ruleChain) {
        if (hasOwnTest(minPriority)) {
            notifier.fireTestStarted(description);
        } else if (children.isEmpty()) {
            notifier.fireTestIgnored(description);
            return;
        }
        try {
            withRules(RuleChain.emptyRuleChain(), this.specificationNode.blockRules()).apply(new Statement() {
                @Override
                public void evaluate() {
                    run(notifier, minPriority, withRules(ruleChain, specificationNode.rules()));
                }
            }, this.description).evaluate();
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(description, e));
        }
        if (hasOwnTest(minPriority)) {
            notifier.fireTestFinished(description);
        }
    }

    private void run(RunNotifier notifier, int minPriority, RuleChain ruleChain) {
        try {
            if (hasOwnTest(minPriority)) {
                ruleChain.apply(specificationNode.test().get().asStatement(), description).evaluate();
            }
        } catch(AssumptionViolatedException e) {
            notifier.fireTestAssumptionFailed(new Failure(description, e));
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(description, e));
        }
        for(ExecutableNode child : children) {
            child.execute(notifier, minPriority, ruleChain);
        }
    }

    private boolean hasOwnTest(int minPriority) {
        return specificationNode.test().isPresent() && priority >= minPriority;
    }

    private RuleChain withRules(RuleChain ruleChain, Stream<TestRule> rules) {
        return rules.reduce(
                ruleChain,
                RuleChain::around,
                (acc1, acc2) -> { throw new RuntimeException("Rules must be defined in a sequential stream");});
    }
}
