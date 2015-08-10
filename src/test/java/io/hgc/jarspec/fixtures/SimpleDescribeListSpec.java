package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JarSpecJUnitRunner.class)
public class SimpleDescribeListSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("Integer arithmetic", by(
            it("supports addition", () -> assertEquals(2, 1+1)),
            it("supports subtraction", () -> assertEquals(3, 8-5)),
            it("supports multiplication", () -> assertEquals(6, 2*3))));
    }
}
