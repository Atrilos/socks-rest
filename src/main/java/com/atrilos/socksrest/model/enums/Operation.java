package com.atrilos.socksrest.model.enums;

import com.atrilos.socksrest.exception.NoSuchOperationException;
import lombok.AllArgsConstructor;

/**
 * Enum-class for comparison operations
 */
@AllArgsConstructor
public enum Operation {
    MORE_THAN("moreThan"), LESS_THAN("lessThan"), EQUAL("equal");

    private final String value;

    /**
     * String to Operation conversion method
     *
     * @param s String name of operation
     * @return Operation object matching given String or Exception if none exist
     */
    public static Operation getByString(String s) {
        for (Operation op : Operation.values()) {
            if (op.value.equals(s)) {
                return op;
            }
        }

        throw new NoSuchOperationException("Operation '%s' wasn't found!".formatted(s));
    }
}
