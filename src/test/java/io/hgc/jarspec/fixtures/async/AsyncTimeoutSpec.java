package io.hgc.jarspec.fixtures.async;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import io.hgc.jarspec.mixins.AsynchronousBehaviour;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(JarSpecJUnitRunner.class)
public class AsyncTimeoutSpec implements Specification, AsynchronousBehaviour {
    @Override
    public SpecificationNode root() {
        return describe("asynchronous behaviour",
                it("should complete fast work within the timeout", () -> {
                    AsyncWork work = new AsyncWork();
                    work.process(10);
                    waitFor(() -> assertEquals(1, work.getResult()));
                }),
                it("should complete slow work within the timeout", () -> {
                    AsyncWork work = new AsyncWork();
                    work.process(100);
                    waitFor(() -> assertEquals(1, work.getResult()));
                })
        );
    }

    @Override
    public int getTimeoutMillis() {
        return 50;
    }
}
