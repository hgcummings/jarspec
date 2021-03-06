package io.hgc.jarspec.fixtures;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JarSpecJUnitRunner.class)
public class AdditionSpec implements Specification {
    @Override
    public SpecificationNode root() {
        return describe("addition",
            describe("of 1+1", () -> {
                int result = 1 + 1;

                return byAllOf(
                        it("equals 2", () -> assertEquals(2, result)),
                        it("equals 3", () -> assertEquals(3, result))
                );
            })
        );
    }
}