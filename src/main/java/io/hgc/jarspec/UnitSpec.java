package io.hgc.jarspec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface UnitSpec {
    public Specification specification();

    default public Specification describe(String unit, BySingle specification) {
        try {
            return new Specification.Aggregate(unit, by(specification.get()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default public Specification describe(String unit, ByMultiple specifications) {
        try {
            return new Specification.Aggregate(unit, specifications.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default public Specification it(String statement, Test test) {
        return new Specification.Single(statement, test);
    }

    default public List<Specification> by(Specification... tests) {
        List<Specification> list = new ArrayList<>();
        Collections.addAll(list, tests);
        return list;
    }
}
