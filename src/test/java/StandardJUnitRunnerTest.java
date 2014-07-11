import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StandardJUnitRunnerTest {
    @Test
    public void testStandardJUnitRunner() {

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
