package io.hgc.jarspec.examples;

import org.junit.*;

public class JUnitTest {
    public JUnitTest() { System.out.println("  constructor"); }

    @BeforeClass public static void beforeClass() { System.out.println("beforeClass"); }

    @Before public void before() { System.out.println("  before"); }

    @Test public void test1() { System.out.println("    test1 execution"); }

    @Test public void test2() { System.out.println("    test2 execution"); }

    @After public void after() { System.out.println("  after"); }

    @AfterClass public static void afterClass() { System.out.println("afterClass"); }
}