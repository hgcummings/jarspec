package io.hgc.jarspec.examples;

import io.hgc.jarspec.UnitSpec;
import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JarSpecJUnitRunner.class)
public class AdditionSpec implements UnitSpec {
    @Override
    public Specification specification() {
        return describe("addition", () ->
            describe("of 1+1", () -> {
                int result = 1 + 1;

                return by(
                    it("should equal 2", () -> assertEquals(2, result)),
                    it("should equal 3", () -> assertEquals(3, result))
                );
            })
        );
    }
}