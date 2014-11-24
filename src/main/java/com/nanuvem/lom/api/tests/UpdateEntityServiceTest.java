package com.nanuvem.lom.api.tests;

import static com.nanuvem.lom.api.tests.EntityHelper.createAndSaveOneEntity;
import static com.nanuvem.lom.api.tests.EntityHelper.createUpdateAndVerifyOneEntity;
import static com.nanuvem.lom.api.tests.EntityHelper.expectExceptionOnInvalidEntityUpdate;

import org.junit.Assert;
import org.junit.Test;

import com.nanuvem.lom.api.Entity;

public abstract class UpdateEntityServiceTest extends LomTestCase {

    @Test
    public void validNewNameAndPackage() {
        createUpdateAndVerifyOneEntity("a", "aaa1", "b", "bbb");
        createUpdateAndVerifyOneEntity("a", "aaa2", "a", "bbb");
        createUpdateAndVerifyOneEntity("a", "aaa3", "b", "aaa");
        createUpdateAndVerifyOneEntity("", "aaa1", "", "bbb");
        createUpdateAndVerifyOneEntity(null, "aaa2", null, "bbb");
        createUpdateAndVerifyOneEntity("a.b.c", "aaa1", "b", "bbb");
        createUpdateAndVerifyOneEntity("a.b.c", "aaa2", "b.c", "bbb");
    }

    @Test
    public void removePackageSetPackage() {
        createUpdateAndVerifyOneEntity("a", "aaa1", "", "aaa");
        createUpdateAndVerifyOneEntity("a", "aaa2", "", "bbb");
        createUpdateAndVerifyOneEntity("", "aaa1", "b", "bbb");
        createUpdateAndVerifyOneEntity("a", "aaa3", null, "aaa");
        createUpdateAndVerifyOneEntity("a", "aaa4", null, "bbb");
        createUpdateAndVerifyOneEntity(null, "aaa2", "b", "bbb");
        createUpdateAndVerifyOneEntity("a", "aaa5", "a", "aaa5");
        createUpdateAndVerifyOneEntity("a", "aaa6", "a", "aaa7");
        createUpdateAndVerifyOneEntity(null, "aaa3", null, "aaa4");
    }

    @Test
    public void renameCausingTwoEntitiesWithSameNameInDifferentPackages() {
        Entity ea = createAndSaveOneEntity("a", "aaa");
        createAndSaveOneEntity("b", "bbb");// TODO Pre-condition

        ea.setName("bbb");
        facade.update(ea);
    }

    @Test
    public void moveCausingTwoEntitiesWithSameNameInDifferentPackages() {
        Entity ea = createAndSaveOneEntity("a", "aaa");
        createAndSaveOneEntity("b", "bbb");// TODO Pre-condition

        ea.setNamespace("c");
        ea.setName("bbb");
        facade.update(ea);
    }

    @Test
    public void newNameAndPackageWithSpaces() {
        Entity ea = createAndSaveOneEntity("a", "aaa");
        expectExceptionOnInvalidEntityUpdate(ea, "name space", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace",
                "name space");
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "na me", INVALID_VALUE_FOR_ENTITY, "name", "na me");
    }

    @Test
    public void removeName() {
        Entity ea = createAndSaveOneEntity("a", "aaa");
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", null, ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "", ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnInvalidEntityUpdate(ea, null, null, ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnInvalidEntityUpdate(ea, null, "", ENTITY_NAME_IS_MANDATORY);
    }

    @Test
    public void renameMoveCausingTwoEntitiesWithSameNameInDefaultPackage() {
        Entity ea = createAndSaveOneEntity("a", "aaa");
        createAndSaveOneEntity("b", "bbb");
        createAndSaveOneEntity("b", "aaa");
        createAndSaveOneEntity("a", "bbb"); // TODO Pre-condition

        expectExceptionOnInvalidEntityUpdate(ea, "b", "bbb", ENTITY_ALREADY_EXISTS, "b.bbb");
        expectExceptionOnInvalidEntityUpdate(ea, "b", "aaa", ENTITY_ALREADY_EXISTS, "b.aaa");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "bbb", ENTITY_ALREADY_EXISTS, "a.bbb");

        Entity e1 = createAndSaveOneEntity("a.b.c", "aaa");
        Entity e2 = createAndSaveOneEntity("b.c", "aaa");
        Entity e3 = createAndSaveOneEntity("a.b.c", "bbb");
        createAndSaveOneEntity("b.c", "bbb"); // TODO Pre-condition

        expectExceptionOnInvalidEntityUpdate(e1, "b.c", "bbb", ENTITY_ALREADY_EXISTS, "b.c.bbb");
        expectExceptionOnInvalidEntityUpdate(e2, "b.c", "bbb", ENTITY_ALREADY_EXISTS, "b.c.bbb");
        expectExceptionOnInvalidEntityUpdate(e3, "b.c", "bbb", ENTITY_ALREADY_EXISTS, "b.c.bbb");
    }

    @Test
    public void renameMoveCausingTwoEntitiesWithSameNameInAnonDefaultPackage() {
        Entity ea1 = createAndSaveOneEntity("a", "aaa");
        Entity ea2 = createAndSaveOneEntity(null, "aaa");
        Entity ea3 = createAndSaveOneEntity("a", "bbb");
        createAndSaveOneEntity(null, "bbb"); // TODO Pre-condition

        expectExceptionOnInvalidEntityUpdate(ea1, null, "bbb", ENTITY_ALREADY_EXISTS, "bbb");
        expectExceptionOnInvalidEntityUpdate(ea2, null, "bbb", ENTITY_ALREADY_EXISTS, "bbb");
        expectExceptionOnInvalidEntityUpdate(ea3, null, "bbb", ENTITY_ALREADY_EXISTS, "bbb");

        Entity ec1 = createAndSaveOneEntity("a.b.c", "ccc");
        Entity ec2 = createAndSaveOneEntity("", "ccc");
        Entity ec3 = createAndSaveOneEntity("a.b.c", "ddd");
        createAndSaveOneEntity("", "ddd"); // TODO Pre-condition

        expectExceptionOnInvalidEntityUpdate(ec1, "", "ddd", ENTITY_ALREADY_EXISTS, "ddd");
        expectExceptionOnInvalidEntityUpdate(ec2, "", "ddd", ENTITY_ALREADY_EXISTS, "ddd");
        expectExceptionOnInvalidEntityUpdate(ec3, "", "ddd", ENTITY_ALREADY_EXISTS, "ddd");
    }

    @Test
    public void renameMoveCausingNameAndPackagesWithInvalidChars() {
        Entity ea = createAndSaveOneEntity("a", "aaa");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa$", INVALID_VALUE_FOR_ENTITY, "name", "aaa$");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa#", INVALID_VALUE_FOR_ENTITY, "name", "aaa#");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa=", INVALID_VALUE_FOR_ENTITY, "name", "aaa=");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa'", INVALID_VALUE_FOR_ENTITY, "name", "aaa'");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa.a", INVALID_VALUE_FOR_ENTITY, "name", "aaa.a");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa/a", INVALID_VALUE_FOR_ENTITY, "name", "aaa/a");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa*", INVALID_VALUE_FOR_ENTITY, "name", "aaa*");
        expectExceptionOnInvalidEntityUpdate(ea, "a$", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a$");
        expectExceptionOnInvalidEntityUpdate(ea, "a#", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a#");
        expectExceptionOnInvalidEntityUpdate(ea, "a=", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a=");
        expectExceptionOnInvalidEntityUpdate(ea, "a'", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a'");
        // expectExceptionOnInvalidEntityUpdate(ea, "a.", "aaa",
        // INVALID_VALUE_FOR_ENTITY, "namespace", "a.");
        expectExceptionOnInvalidEntityUpdate(ea, "a/a", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a/a");
        expectExceptionOnInvalidEntityUpdate(ea, "a*", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a*");
    }

    @Test
    public void renameMoveForcingCaseInsentivePackagesAndNames() {
        Entity ea = createAndSaveOneEntity("a", "aaa");
        createAndSaveOneEntity("b", "bbb");
        createAndSaveOneEntity("CcC", "ccc");
        createAndSaveOneEntity("DDD", "ddd"); // TODO Pre-condition

        expectExceptionOnInvalidEntityUpdate(ea, "b", "BbB", ENTITY_ALREADY_EXISTS, "b.bbb");
        expectExceptionOnInvalidEntityUpdate(ea, "b", "BBB", ENTITY_ALREADY_EXISTS, "b.bbb");
        expectExceptionOnInvalidEntityUpdate(ea, "ccc", "ccc", ENTITY_ALREADY_EXISTS, "ccc.ccc");
        expectExceptionOnInvalidEntityUpdate(ea, "CCC", "ccc", ENTITY_ALREADY_EXISTS, "ccc.ccc");
        expectExceptionOnInvalidEntityUpdate(ea, "ddd", "ddd", ENTITY_ALREADY_EXISTS, "ddd.ddd");
        expectExceptionOnInvalidEntityUpdate(ea, "ddd", "DDD", ENTITY_ALREADY_EXISTS, "ddd.ddd");
    }

    @Test
    public void invalidIdAndVersion() {
        Entity ea = createAndSaveOneEntity("a", "aaa"); // TODO Pre-condition
        Long originalId = ea.getId();
        Integer originalVersion = ea.getVersion();

        ea.setId(null);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name", "The id of an Entity is mandatory on update");

        ea.setId(originalId);
        ea.setVersion(null);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name", "The version of an Entity is mandatory on update");

        ea.setId(null);
        ea.setVersion(null);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name",
                "The version and id of an Entity are mandatory on update");

        ea.setId(originalId + 1);
        ea.setVersion(originalVersion);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name", "Invalid id for Entity namespace.name");

        ea.setId(originalId);
        ea.setVersion(originalVersion - 1);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name", "Updating a deprecated version of Entity a.aaa. "
                + "Get the Entity again to obtain the newest version and proceed updating.");
    }

    @Test
    public void severalUpdates() {
        Entity ea = createAndSaveOneEntity("a", "aaa"); // TODO Pre-condition

        ea.setNamespace("b");
        ea.setName("abc");
        ea = facade.update(ea);

        ea.setNamespace("a.b.d");
        ea.setName("abc");
        ea = facade.update(ea);

        ea.setNamespace(null);
        ea.setName("abc");
        ea = facade.update(ea);

        ea.setNamespace("a.b.c");
        ea.setName("abc");
        ea = facade.update(ea);

        Entity found = facade.findEntityById(ea.getId());
        Assert.assertEquals("a.b.c", found.getNamespace());
        Assert.assertEquals("abc", found.getName());
        Assert.assertEquals(new Long(1), found.getId());
        Assert.assertEquals(new Integer(4), found.getVersion());
    }
}
