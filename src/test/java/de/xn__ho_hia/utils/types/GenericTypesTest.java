/*
 * This file is part of generic-types. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of generic-types,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.utils.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.xn__ho_hia.quality.suppression.CompilerWarnings;

/**
 *
 */
@SuppressWarnings({ CompilerWarnings.NLS, CompilerWarnings.STATIC_METHOD })
public class GenericTypesTest {

    /**
     *
     */
    @Test
    public void shouldCreateGenericList() {
        // given
        final Type genericType = List.class;
        final Type elementType = String.class;

        // when
        final Type type = GenericTypes.generic(genericType, elementType);

        // then
        Assert.assertEquals("Types do not match", "java.util.List<java.lang.String>", type.getTypeName());
    }

    /**
     *
     */
    @Test
    public void shouldCreateGenericMap() {
        // given
        final Type genericType = Map.class;
        final Type keyType = String.class;
        final Type valueType = String.class;

        // when
        final Type type = GenericTypes.generic(genericType, keyType, valueType);

        // then
        Assert.assertEquals("Types do not match", "java.util.Map<java.lang.String, java.lang.String>",
                type.getTypeName());
    }

    /**
     *
     */
    @Test
    public void shouldCreateGenericSet() {
        // given
        final Type genericType = Set.class;
        final Type elementType = String.class;

        // when
        final Type type = GenericTypes.generic(genericType, elementType);

        // then
        Assert.assertEquals("Types do not match", "java.util.Set<java.lang.String>", type.getTypeName());
    }

    /**
     *
     */
    @Test
    public void shouldCreateNestedGenericSet() {
        // given
        final Type genericType = Set.class;
        final Type elementType = GenericTypes.generic(genericType, String.class);

        // when
        final Type type = GenericTypes.generic(genericType, elementType);

        // then
        Assert.assertEquals("Types do not match", "java.util.Set<java.util.Set<java.lang.String>>", type.getTypeName());
    }

    /**
     *
     */
    @Test
    public void shouldCreateGenericWildcardSubtypeList() {
        // given
        final Type genericType = List.class;
        final Type elementType = GenericTypes.subtype(String.class);

        // when
        final Type type = GenericTypes.generic(genericType, elementType);

        // then
        Assert.assertEquals("Types do not match", "java.util.List<? extends java.lang.String>", type.getTypeName());
    }

    /**
     *
     */
    @Test
    public void shouldCreateGenericWildcardSupertypeList() {
        // given
        final Type genericType = List.class;
        final Type elementType = GenericTypes.supertype(String.class);

        // when
        final Type type = GenericTypes.generic(genericType, elementType);

        // then
        Assert.assertEquals("Types do not match", "java.util.List<? super java.lang.String>", type.getTypeName());
    }

    /**
     *
     */
    @Test
    public void shouldCreateNestedGenericWildcardSupertypeList() {
        // given
        final Type genericType = List.class;
        final Type elementType = GenericTypes.generic(genericType, GenericTypes.supertype(String.class));

        // when
        final Type type = GenericTypes.generic(genericType, elementType);

        // then
        Assert.assertEquals("Types do not match", "java.util.List<java.util.List<? super java.lang.String>>",
                type.getTypeName());
    }

    /**
     * Ensures that the constructor of the {@link GenericTypes} class is private.
     * <p>
     * The class should never be instantiated. Instead use the static factory methods to construct storage units.
     *
     * @throws NoSuchMethodException
     *             Should not fail in case the StorageUnits class has a constructor..
     * @throws IllegalAccessException
     *             Should not fail in case the StorageUnits class has a constructor..
     * @throws InvocationTargetException
     *             Should not fail in case the StorageUnits class has a constructor..
     * @throws InstantiationException
     *             Should not fail in case the StorageUnits class has a constructor..
     */
    @Test
    @SuppressWarnings(CompilerWarnings.NULL)
    public void shouldDeclarePrivateConstructor()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        final Constructor<GenericTypes> constructor = GenericTypes.class.getDeclaredConstructor();

        // When
        final boolean isPrivate = Modifier.isPrivate(constructor.getModifiers());

        // Then
        Assert.assertTrue("Constructor is not private", isPrivate);
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}
