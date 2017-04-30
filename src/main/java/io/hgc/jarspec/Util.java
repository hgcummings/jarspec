package io.hgc.jarspec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class Util {
    private Util() {} // Utility classes should not be instantiable
    /**
     * Returns an Exception for any Throwable object, wrapping in a RuntimeException if necessary
     * @param throwable any Throwable object
     * @return the original or wrapped Exception
     */
    static Exception exceptionFrom(Throwable throwable) {
        if (throwable instanceof Exception) {
            return (Exception) throwable;
        } else {
            return new RuntimeException(throwable);
        }
    }
}