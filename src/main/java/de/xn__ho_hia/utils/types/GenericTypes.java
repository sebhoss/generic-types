/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org>
 */
package de.xn__ho_hia.utils.types;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Factory to create generic {@link Type} variations, such as <code>Map&lt;String, Object&gt;</code>. Use it as follows:
 *
 * <pre>
 * final Type genericMap = GenericTypes.generic(Map.class, String.class, Object.class);
 * final Type genericList = GenericTypes.generic(List.class, String.class);
 * </pre>
 *
 * Super- and subtypes such as <code>List&lt;? super Point&gt;</code> or <code>List&lt;? extends Number&gt;</code> can
 * be created in the following way:
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

    /**
     * Factory method to create a generic {@link Type}.
     *
     * @param type
     *            The outer type.
     * @param types
     *            The generic type parameters of the outer type.
     * @return A {@link Type} representing the given types.
     */
    public static final Type generic(final Type type, final Type... types) {
        return genericType(type, types);
    }

    /**
     * Factory method to create a generic subtype.
     *
     * @param type
     *            The supertype.
     * @return A {@link Type} representing any given subtype of the supplied supertype.
     */
    public static final Type subtype(final Type type) {
        return typeExtends(wildcardType(), type);
    }

    /**
     * Factory method to create a generic supertype.
     *
     * @param type
     *            The subtype.
     * @return A {@link Type} representing any given supertype of the supplied subtype.
     */
    public static final Type supertype(final Type type) {
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

    private GenericTypes() {
        // factory class
    }

    /**
     * Wrapper around {@link Type} in order to be able to create them using lambda expressions (see above)
     */
    @FunctionalInterface
    private static interface LambdaEnabledType extends Type {

        @Override
        String getTypeName();

    }

}
