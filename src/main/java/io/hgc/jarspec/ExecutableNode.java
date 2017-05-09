package io.hgc.jarspec;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.RuleChain;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Internal class. Adapts a {@link SpecificationNode} into a form that can be executed by junit.
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

    private RuleChain withOwnRules(RuleChain ruleChain) {
        return this.specificationNode.rules().reduce(
                ruleChain,
                RuleChain::around,
                (acc1, acc2) -> { throw new RuntimeException("Rules must be defined in sequence!");});
    }

    private void execute(RunNotifier notifier, int minPriority, RuleChain ruleChain) {
        ruleChain = withOwnRules(ruleChain);
        Optional<Test> test = specificationNode.test();
        if ((test.isPresent() && priority >= minPriority) || !children.isEmpty()) {
            notifier.fireTestStarted(description);
        } else {
            notifier.fireTestIgnored(description);
            return;
        }
        try {
            if (test.isPresent() && priority >= minPriority) {
                ruleChain.apply(test.get().asStatement(), description).evaluate();
            }
        } catch(AssumptionViolatedException e) {
            notifier.fireTestAssumptionFailed(new Failure(description, e));
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(description, e));
        }
        for(ExecutableNode child : children) {
            child.execute(notifier, minPriority, ruleChain);
        }
        notifier.fireTestFinished(description);
    }
}
