package com.nanuvem.lom.api.tests.entity;

import static com.nanuvem.lom.api.tests.entity.EntityHelper.createAndSaveOneEntity;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.createAndVerifyOneEntity;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.createAndVerifyTwoEntities;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.expectExceptionOnCreateInvalidEntity;

import org.junit.Test;

import com.nanuvem.lom.api.tests.LomTestCase;

public abstract class CreateEntityTest extends LomTestCase {

    @Test
    public void validNameAndNamespace() {
        createAndVerifyOneEntity("abc", "abc");
        createAndVerifyOneEntity("a.b.c", "abc");
        createAndVerifyOneEntity("a", "a");
        createAndVerifyOneEntity("abc123", "aaa");
        createAndVerifyOneEntity("abc", "abc1122");
    }

    @Test
    public void withoutNamespace() {
        createAndVerifyOneEntity("", "abc");
        createAndVerifyOneEntity(null, "abc");
        createAndVerifyOneEntity("", "a1");
        createAndVerifyOneEntity(null, "a1");
    }

    @Test
    public void twoEntitiesWithSameNameInDifferentNamespaces() {
        createAndVerifyTwoEntities("p1", "name", "p2", "name");
        createAndVerifyTwoEntities(null, "name", "p1", "name");
        createAndVerifyTwoEntities("a", "name", "a.b", "name");
    }

    @Test
    public void nameAndNamespaceWithSpaces() {
        expectExceptionOnCreateInvalidEntity("name space", "name", INVALID_VALUE_FOR_ENTITY, "namespace", "name space");
        expectExceptionOnCreateInvalidEntity("namespace", "na me", INVALID_VALUE_FOR_ENTITY, "name", "na me");
    }

    @Test
    public void withoutName() {
        expectExceptionOnCreateInvalidEntity("namespace", null, ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnCreateInvalidEntity("namespace", "", ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnCreateInvalidEntity(null, null, ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnCreateInvalidEntity("", null, ENTITY_NAME_IS_MANDATORY);
    }

    @Test
    public void twoEntitiesWithSameNameInDefaultNamespace() {
        createAndSaveOneEntity(null, "aaa");
        expectExceptionOnCreateInvalidEntity(null, "aaa", ENTITY_ALREADY_EXISTS, "aaa");
        expectExceptionOnCreateInvalidEntity("", "aaa", ENTITY_ALREADY_EXISTS, "aaa");
    }

    @Test
    public void twoEntitiesWithSameNameInAnonDefaultNamespace() {
        createAndSaveOneEntity("a", "aaa");
        expectExceptionOnCreateInvalidEntity("a", "aaa", ENTITY_ALREADY_EXISTS, "a.aaa");
    }

    @Test
    public void nameAndNamespaceWithInvalidChars() {
        expectExceptionOnCreateInvalidEntity("a", "aaa$", INVALID_VALUE_FOR_ENTITY, "name", "aaa$");
        expectExceptionOnCreateInvalidEntity("a", "aaa#", INVALID_VALUE_FOR_ENTITY, "name", "aaa#");
        expectExceptionOnCreateInvalidEntity("a", "aaa=", INVALID_VALUE_FOR_ENTITY, "name", "aaa=");
        expectExceptionOnCreateInvalidEntity("a", "aaa.a", INVALID_VALUE_FOR_ENTITY, "name", "aaa.a");
        expectExceptionOnCreateInvalidEntity("a", "aaa/a", INVALID_VALUE_FOR_ENTITY, "name", "aaa/a");
        expectExceptionOnCreateInvalidEntity("a", "aaa*", INVALID_VALUE_FOR_ENTITY, "name", "aaa*");
        expectExceptionOnCreateInvalidEntity("a", "aaa'", INVALID_VALUE_FOR_ENTITY, "name", "aaa'");
        expectExceptionOnCreateInvalidEntity("a$", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a$");
        expectExceptionOnCreateInvalidEntity("a#", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a#");
        expectExceptionOnCreateInvalidEntity("a=", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a=");
        expectExceptionOnCreateInvalidEntity("a'", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a'");
        // expectExceptionOnCreateInvalidEntity("a.",
        // "aaa",INVALID_VALUE_FOR_ENTITY, "namespace", "a.");
        expectExceptionOnCreateInvalidEntity("a/a", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a/a");
        expectExceptionOnCreateInvalidEntity("a*", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a*");
    }

}
