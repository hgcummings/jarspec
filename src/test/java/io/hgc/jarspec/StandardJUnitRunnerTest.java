package io.hgc.jarspec;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

import static io.hgc.jarspec.JarSpecJUnitRunnerTest.verifyDescriptionTestClass;
import static org.junit.Assert.assertEquals;

public class StandardJUnitRunnerTest {
    private Runner runner;

    @Before
    public void setup() throws InitializationError {
        runner = new JUnit4(TestClass.class);
    }

    @Test
    public void testCountShouldMatchNumberOfTests() throws InitializationError {
        assertEquals(2, runner.testCount());
    }

    @Test
    public void descriptionTestClassShouldMatchProvidedClass() {
        Description description = runner.getDescription();

        verifyDescriptionTestClass(description, TestClass.class);
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
