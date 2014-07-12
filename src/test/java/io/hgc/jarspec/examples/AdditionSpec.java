package io.hgc.jarspec.examples;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import org.junit.runner.RunWith;

import java.util.function.Supplier;

import static io.hgc.jarspec.Specification.describe;
import static io.hgc.jarspec.Specification.describes;
import static io.hgc.jarspec.Specification.it;
import static org.junit.Assert.assertEquals;

@RunWith(JarSpecJUnitRunner.class)
public class AdditionSpec implements Supplier<Specification> {
    @Override
    public Specification get() {
        return describe("addition", () -> {
            return describes("of 1+1", () -> {
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