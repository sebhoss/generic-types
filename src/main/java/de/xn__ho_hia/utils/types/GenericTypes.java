/*
 * This file is part of generic-types. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of generic-types,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.utils.types;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Factory to create generic {@link Type} variations, such as
 * <code>Map&lt;String, Object&gt;</code>. Use it as follows:
 *
 * <pre>
 * final Type genericMap = GenericTypes.generic(Map.class, String.class, Object.class);
 * final Type genericList = GenericTypes.generic(List.class, String.class);
 * </pre>
 *
 * Super- and subtypes such as <code>List&lt;? super Point&gt;</code> or
 * <code>List&lt;? extends Number&gt;</code> can be created in the following
 * way:
 *
 * <pre>
 * final Type list = GenericTypes.generic(List.class, GenericTypes.supertype(Point.class));
 * final Type list = GenericTypes.generic(List.class, GenericTypes.subtype(Number.class));
 * </pre>
 *
 * Use static imports to shorten the above calls to:
 *
 * <pre>
 * final Type list = generic(List.class, supertype(Point.class));
 * final Type list = generic(List.class, subtype(Number.class));
 * </pre>
 *
 * and then go crazy with this:
 *
 * <pre>
 * final Type type = generic(List.class, generic(Map.class, subtype(Number.class), supertype(String.class)));
 * // represents List&lt;Map&lt;? extends Number, ? super String&gt;&gt;
 * </pre>
 */
public final class GenericTypes {

    private GenericTypes() {
        // factory class
    }

    /**
     * Factory method to create a generic {@link Type}.
     *
     * @param type
     *            The outer type.
     * @param types
     *            The generic type parameters of the outer type.
     * @return A {@link Type} representing the given types.
     */
    public static Type generic(final Type type, final Type... types) {
        return genericType(type, types);
    }

    /**
     * Factory method to create a generic subtype.
     *
     * @param type
     *            The supertype.
     * @return A {@link Type} representing any given subtype of the supplied
     *         supertype.
     */
    public static Type subtype(final Type type) {
        return typeExtends(wildcardType(), type);
    }

    /**
     * Factory method to create a generic supertype.
     *
     * @param type
     *            The subtype.
     * @return A {@link Type} representing any given supertype of the supplied
     *         subtype.
     */
    public static Type supertype(final Type type) {
        return typeSuper(wildcardType(), type);
    }

    private static LambdaEnabledType genericType(final Type type, final Type... types) {
        return () -> String.format("%1$s<%2$s>", type.getTypeName(), commaDelimited(types)); //$NON-NLS-1$
    }

    private static String commaDelimited(final Type... types) {
        return Arrays.stream(types)
                .map(Type::getTypeName)
                .collect(Collectors.joining(", ")); //$NON-NLS-1$
    }

    private static LambdaEnabledType typeExtends(final Type left, final Type right) {
        return () -> String.format("%1$s extends %2$s", left.getTypeName(), right.getTypeName()); //$NON-NLS-1$
    }

    private static LambdaEnabledType typeSuper(final Type left, final Type right) {
        return () -> String.format("%1$s super %2$s", left.getTypeName(), right.getTypeName()); //$NON-NLS-1$
    }

    private static LambdaEnabledType wildcardType() {
        return () -> "?"; //$NON-NLS-1$
    }

    /**
     * Wrapper around {@link Type} in order to be able to create them using
     * lambda expressions (see above)
     */
    @FunctionalInterface
    private static interface LambdaEnabledType extends Type {

        @Override
        String getTypeName();

    }

}
