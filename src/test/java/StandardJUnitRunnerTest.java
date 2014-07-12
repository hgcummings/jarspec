import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

import static org.junit.Assert.assertEquals;

public class StandardJUnitRunnerTest {
    @Test
    public void testStandardJUnitRunner() throws InitializationError {
        Runner runner = new JUnit4(TestClass.class);
        RunNotifier runNotifier = new RunNotifier();

        assertEquals(2, runner.testCount());
    }

    public static class TestClass {
        private int result;

        @Before
        public void setup() {
            result = 1 + 1;
        }

        @Test
        public void onePlusOneEqualsTwo() {
            assertEquals(result, 2);
        }

        @Test
        public void onePlusOneEqualsThree() {
            assertEquals(result, 3);
        }
    }
}
