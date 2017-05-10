package io.hgc.jarspec.fixtures.rules;

import io.hgc.jarspec.JarSpecJUnitRunner;
import io.hgc.jarspec.Specification;
import io.hgc.jarspec.SpecificationNode;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(JarSpecJUnitRunner.class)
public class ExpensiveDependencySpec implements Specification {

    @Override
    public SpecificationNode root() {
        Server server = new Server();
        return describe("Spec with expensive external dependency",
                describe("Block with dependency",
                        it("initialises dependency before first test", () -> assertNotNull(server.get())),
                        it("retains dependency between tests", () -> assertNotNull(server.get())))
                .withBlockRule(server),
                it("tears down dependency at end of block", () -> assertFalse(server.running))
        );
    }

    public static class Server extends ExternalResource {
        private boolean running;

        @Override
        public void before() { this.start(); }

        @Override
        public void after() { this.stop(); }

        public void start() {
            running = true;
        }

        public String get() {
            if (running) {
                return "HTTP 200 OK";
            } else {
                throw new IllegalStateException("Server not started");
            }
        }

        public void stop() {
            if (running) {
                running = false;
            } else {
                throw new IllegalStateException("Server already stopped (or never started)");
            }
        }

    }
}
