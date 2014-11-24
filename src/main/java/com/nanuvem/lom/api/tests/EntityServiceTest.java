package com.nanuvem.lom.api.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.MetadataException;

import static com.nanuvem.lom.api.tests.EntityHelper.*;
import static org.junit.Assert.fail;

public abstract class EntityServiceTest {

    private static final String ENTITY_ALREADY_EXISTS = "The %1$s Entity already exists";
    private static final String ENTITY_NAME_IS_MANDATORY = "The name of an Entity is mandatory";
    private static final String INVALID_VALUE_FOR_ENTITY = "Invalid value for Entity %1$s: %2$s";
    private Facade facade;

    public abstract Facade createFacade();

    @Before
    public void init() {
        facade = createFacade();
        EntityHelper.setFacade(facade);
    }

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
        createAndSaveOneEntity(null, "aaa"); // TODO Pre-condition
        expectExceptionOnCreateInvalidEntity(null, "aaa", ENTITY_ALREADY_EXISTS, "aaa");
        expectExceptionOnCreateInvalidEntity("", "aaa", ENTITY_ALREADY_EXISTS, "aaa");
    }

    @Test
    public void twoEntitiesWithSameNameInAnonDefaultNamespace() {
        createAndSaveOneEntity("a", "aaa"); // TODO Pre-condition
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

    @Test
    public void listallEntities() {
        List<Entity> allEntities = facade.listAllEntities();
        Assert.assertEquals(0, allEntities.size());

        Entity entity1 = createAndSaveOneEntity("ns1", "n1");
        Entity entity2 = createAndSaveOneEntity("ns2", "n2");
        Entity entity3 = createAndSaveOneEntity("ns2", "n3");

        allEntities = facade.listAllEntities();

        Assert.assertEquals(3, allEntities.size());
        Assert.assertEquals(entity1, allEntities.get(0));
        Assert.assertEquals(entity2, allEntities.get(1));
        Assert.assertEquals(entity3, allEntities.get(2));

        facade.deleteEntity(entity1.getId());
        facade.deleteEntity(entity2.getId());
        facade.deleteEntity(entity3.getId());

        Assert.assertEquals(0, facade.listAllEntities().size());
    }

    @Test
    public void listEntitiesByValidFragmentOfNameAndPackage() {
        String namespaceFragment = "ns";
        String nameFragment = "n";

        List<Entity> allEntities = facade.listEntitiesByFullName(namespaceFragment);
        Assert.assertEquals(0, allEntities.size());

        allEntities = facade.listEntitiesByFullName(nameFragment);
        Assert.assertEquals(0, allEntities.size());

        Entity entity1 = createAndSaveOneEntity("ns1", "n1");
        Entity entity2 = createAndSaveOneEntity("ns2", "n2");
        Entity entity3 = createAndSaveOneEntity("ns2", "n3");

        allEntities = facade.listEntitiesByFullName(namespaceFragment);
        Assert.assertEquals(3, allEntities.size());
        Assert.assertEquals(entity1, allEntities.get(0));
        Assert.assertEquals(entity2, allEntities.get(1));
        Assert.assertEquals(entity3, allEntities.get(2));

        allEntities = facade.listEntitiesByFullName(nameFragment);
        Assert.assertEquals(3, allEntities.size());
        Assert.assertEquals(entity1, allEntities.get(0));
        Assert.assertEquals(entity2, allEntities.get(1));
        Assert.assertEquals(entity3, allEntities.get(2));

        facade.deleteEntity(entity1.getId());
        facade.deleteEntity(entity2.getId());
        facade.deleteEntity(entity3.getId());

        Assert.assertEquals(0, facade.listAllEntities().size());
    }

    @Test
    public void listEntitiesByEmptyNameAndPackage() {
        List<Entity> allEntities = facade.listEntitiesByFullName("");
        Assert.assertEquals(0, allEntities.size());

        Entity entity1 = createAndSaveOneEntity("ns1", "n1");
        Entity entity2 = createAndSaveOneEntity("ns2", "n2");
        Entity entity3 = createAndSaveOneEntity("ns2", "n3");

        List<Entity> allEntities1 = facade.listEntitiesByFullName("");
        Assert.assertEquals(3, allEntities1.size());
        Assert.assertEquals(entity1, allEntities1.get(0));
        Assert.assertEquals(entity2, allEntities1.get(1));
        Assert.assertEquals(entity3, allEntities1.get(2));

        allEntities = facade.listEntitiesByFullName("nspace");
        Assert.assertEquals(0, allEntities.size());

        facade.deleteEntity(entity1.getId());
        facade.deleteEntity(entity2.getId());
        facade.deleteEntity(entity3.getId());

        Assert.assertEquals(0, facade.listAllEntities().size());
    }

    @Test
    public void listEntitiesByFragmentOfNameAndPackagesWithSpaces() {
        expectExceptionOnInvalidEntityList("na me", "Invalid value for Entity full name: na me");
    }

    @Test
    public void listEntitiesForcingCaseInsensitivePackagesAndNames() {
        Entity entity1 = createAndSaveOneEntity("ns1", "n1");
        Entity entity2 = createAndSaveOneEntity("NS2", "n2");
        Entity entity3 = createAndSaveOneEntity("NS3", "N3");
        List<Entity> expectedEntities = new ArrayList<Entity>();
        expectedEntities.add(entity1);
        expectedEntities.add(entity2);
        expectedEntities.add(entity3);

        List<Entity> allEntities1 = facade.listEntitiesByFullName("ns");
        Assert.assertEquals(3, allEntities1.size());
        Assert.assertEquals(entity1, allEntities1.get(0));
        Assert.assertEquals(entity2, allEntities1.get(1));
        Assert.assertEquals(entity3, allEntities1.get(2));

        allEntities1 = facade.listEntitiesByFullName("n");
        Assert.assertEquals(3, allEntities1.size());
        Assert.assertEquals(entity1, allEntities1.get(0));
        Assert.assertEquals(entity2, allEntities1.get(1));
        Assert.assertEquals(entity3, allEntities1.get(2));

        allEntities1 = facade.listEntitiesByFullName("NS");
        Assert.assertEquals(3, allEntities1.size());
        Assert.assertEquals(entity1, allEntities1.get(0));
        Assert.assertEquals(entity2, allEntities1.get(1));
        Assert.assertEquals(entity3, allEntities1.get(2));

        allEntities1 = facade.listEntitiesByFullName("N");
        Assert.assertEquals(3, allEntities1.size());
        Assert.assertEquals(entity1, allEntities1.get(0));
        Assert.assertEquals(entity2, allEntities1.get(1));
        Assert.assertEquals(entity3, allEntities1.get(2));
    }

    @Test
    public void listEntitiesUsingInvalidFragmentOfNameAndPackage() {
        expectExceptionOnInvalidEntityList("n$", INVALID_VALUE_FOR_ENTITY, "full name", "n$");
        expectExceptionOnInvalidEntityList("n#", INVALID_VALUE_FOR_ENTITY, "full name", "n#");
        expectExceptionOnInvalidEntityList("n=", INVALID_VALUE_FOR_ENTITY, "full name", "n=");
        expectExceptionOnInvalidEntityList("n'", INVALID_VALUE_FOR_ENTITY, "full name", "n'");
        expectExceptionOnInvalidEntityList("n/n", INVALID_VALUE_FOR_ENTITY, "full name", "n/n");
        expectExceptionOnInvalidEntityList("n*", INVALID_VALUE_FOR_ENTITY, "full name", "n*");
    }

    @Test
    public void getEntityByValidNameAndPackage() {
        Assert.assertEquals(0, facade.listEntitiesByFullName("ns.n").size());

        Entity entity1 = createEntity("ns1", "n1");
        Entity foundEntity1 = facade.findEntityByFullName("ns1.n1");
        Assert.assertEquals(entity1, foundEntity1);

        Entity entity2 = createEntity("ns2", "n2");
        Entity foundEntity2 = facade.findEntityByFullName("ns2.n2");
        Assert.assertEquals(entity2, foundEntity2);

        Assert.assertEquals(1, facade.listEntitiesByFullName("ns1.n").size());
        Assert.assertEquals(0, facade.listEntitiesByFullName("ns.n1").size());
        Assert.assertEquals(0, facade.listEntitiesByFullName("ns2.n1").size());

        List<Entity> allEntities = facade.listAllEntities();
        Assert.assertEquals(2, allEntities.size());
        Assert.assertEquals(entity1, allEntities.get(0));
        Assert.assertEquals(entity2, allEntities.get(1));
    }

    @Test
    public void getEntityByEmptyNameAndPackage() {
        createEntity("ns1", "n1");
        Entity entity2 = createEntity(null, "n2");
        Assert.assertEquals(1, facade.listEntitiesByFullName(".n1").size());

        Entity foundEntity2 = facade.findEntityByFullName("n2");
        Assert.assertEquals(entity2, foundEntity2);
        Assert.assertEquals(1, facade.listEntitiesByFullName("ns1.").size());
    }

    @Test
    public void getEntityByNameAndPackageWithSpaces() {
        expectExceptionOnInvalidEntityList(".na me", INVALID_VALUE_FOR_ENTITY, "full name", ".na me");
        expectExceptionOnInvalidEntityList("name space.name", INVALID_VALUE_FOR_ENTITY, "full name", "name space.name");
        expectExceptionOnInvalidEntityList("namespace.na me", INVALID_VALUE_FOR_ENTITY, "full name", "namespace.na me");
    }

    @Test
    public void getEntityForcingCaseInsensitivePackagesAndNames() {
        Entity entity = createEntity("nS", "nA");
        Entity ea = facade.findEntityByFullName("ns.na");
        Assert.assertEquals(entity, ea);

        ea = facade.findEntityByFullName("NS.NA");
        Assert.assertEquals(entity, ea);

        ea = facade.findEntityByFullName("nS.nA");
        Assert.assertEquals(entity, ea);

        ea = facade.findEntityByFullName("NS.na");
        Assert.assertEquals(entity, ea);

        ea = facade.findEntityByFullName("ns.NA");
        Assert.assertEquals(entity, ea);

        ea = facade.findEntityByFullName("Ns.Na");
        Assert.assertEquals(entity, ea);

    }

    @Test
    public void getEntityUsingInvalidNameAndPackage() {
        expectExceptionOnInvalidEntityList("ns.n$", INVALID_VALUE_FOR_ENTITY, "full name", "ns.n$");
        expectExceptionOnInvalidEntityList("ns.n#", INVALID_VALUE_FOR_ENTITY, "full name", "ns.n#");
        expectExceptionOnInvalidEntityList("ns.n=", INVALID_VALUE_FOR_ENTITY, "full name", "ns.n=");
        expectExceptionOnInvalidEntityList("ns.n/n", INVALID_VALUE_FOR_ENTITY, "full name", "ns.n/n");
        expectExceptionOnInvalidEntityList("ns.n*", INVALID_VALUE_FOR_ENTITY, "full name", "ns.n*");
        expectExceptionOnInvalidEntityList("ns.n'", INVALID_VALUE_FOR_ENTITY, "full name", "ns.n'");
        expectExceptionOnInvalidEntityList("ns$.n", INVALID_VALUE_FOR_ENTITY, "full name", "ns$.n");
        expectExceptionOnInvalidEntityList("ns#.n", INVALID_VALUE_FOR_ENTITY, "full name", "ns#.n");
        expectExceptionOnInvalidEntityList("ns=.n", INVALID_VALUE_FOR_ENTITY, "full name", "ns=.n");
        expectExceptionOnInvalidEntityList("ns/.n", INVALID_VALUE_FOR_ENTITY, "full name", "ns/.n");
        expectExceptionOnInvalidEntityList("ns*.n", INVALID_VALUE_FOR_ENTITY, "full name", "ns*.n");
        expectExceptionOnInvalidEntityList("ns'.n", INVALID_VALUE_FOR_ENTITY, "full name", "ns'.n");
    }

    @Test
    public void deleteEntity() {
        Entity c = createEntity("a", "aaa"); // TODO Pre-condition
        facade.deleteEntity(c.getId());

        try {
            facade.deleteEntity(c.getId());
            fail();
        } catch (MetadataException e) {
            Assert.assertEquals("Unknown id for Entity: 1", e.getMessage());
        }

        try {
            facade.deleteEntity(c.getId() + 10);
            fail();
        } catch (MetadataException e) {
            Assert.assertEquals("Unknown id for Entity: 11", e.getMessage());
        }

    }
}
