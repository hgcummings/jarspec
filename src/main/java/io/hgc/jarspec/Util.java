package io.hgc.jarspec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class Util {
    private Util() {} // Utility classes should not be instantiable

    /**
     * Creates an immutable copy of a list, taking a defensive copy of
     * the original list and returning an unmodifiable view of the copy.
     *
     * The elements contained within the list may themselves be mutable.
     * It is up to the caller to ensure that they are immutable if desired.
     *
     * @param original the original, possibly mutable list
     * @param <T> the type of the list elements
     * @return an immutable copy of the original list
     */
    static <T> List<T> immutableCopy(List<T> original) {
        List<T> copy = new ArrayList<>();
        copy.addAll(original);
        return Collections.unmodifiableList(copy);
    }
}
