import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.JarSpecTest;
import io.hgc.jarspec.Specification;
import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.util.function.Supplier;

import static io.hgc.jarspec.Specification.describe;
import static io.hgc.jarspec.Specification.describes;
import static io.hgc.jarspec.Specification.it;
import static org.junit.Assert.assertEquals;

public class JarSpecJUnitRunnerTest {
    @Test
    public void testStandardJUnitRunner() throws InitializationError {
        Runner runner = new JarSpecJUnitRunner(TestClass.class);
        RunNotifier runNotifier = new RunNotifier();

        assertEquals(2, runner.testCount());
    }

    public static class TestClass implements Supplier<Specification> {
        @Override
        public Specification get() {
            return describe("addition", () -> {
                return describes("1+1", () -> {
                    int result = 1 + 1;

                    return new Specification[]{
                        it("should equal 2", () -> {
                            assertEquals(2, result);
                        }),
                        it("should equal 3", () -> {
                            assertEquals(3, result);
                        })
                    };
                });
            });
        }
    }
}