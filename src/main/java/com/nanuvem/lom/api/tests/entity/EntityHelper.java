package com.nanuvem.lom.api.tests.entity;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;

import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.MetadataException;

public class EntityHelper {

    private static Facade facade;

    public static EntityType createEntity(String namespace, String name) {
        EntityType entityType = new EntityType();
        entityType.setName(name);
        entityType.setNamespace(namespace);
        entityType = facade.create(entityType);
        return entityType;
    }

    public static void expectExceptionOnInvalidFindEntityByFullName(String fullName, String expectedMessage) {
        try {
            facade.findEntityByFullName(fullName);
            fail();
        } catch (MetadataException me) {
            Assert.assertEquals(expectedMessage, me.getMessage());
        }
    }

    public static void expectExceptionOnInvalidEntityList(String fragment, String expectedMessage, String... args) {
        try {
            facade.listEntitiesByFullName(fragment);
            fail();
        } catch (MetadataException e) {
            String formatedMessage = String.format(expectedMessage, (Object[]) args);
            Assert.assertEquals(formatedMessage, e.getMessage());
        }
    }

    public static void expectExceptionOnInvalidEntityUpdate(EntityType entityType, String secondnamespace, String secondname,
            String expectedMessage, String... args) {

        try {
            entityType.setNamespace(secondnamespace);
            entityType.setName(secondname);
            facade.update(entityType);
            fail();
        } catch (MetadataException e) {
            String formatedMessage = String.format(expectedMessage, (Object[]) args);
            Assert.assertEquals(formatedMessage, e.getMessage());
        }
    }

    public static void expectExceptionOnCreateInvalidEntity(String namespace, String name, String expectedMessage,
            String... args) {
        try {
            createAndVerifyOneEntity(namespace, name);
            fail();
        } catch (MetadataException e) {
            String formatedMessage = String.format(expectedMessage, (Object[]) args);
            Assert.assertEquals(formatedMessage, e.getMessage());
        }
    }

    public static void createUpdateAndVerifyOneEntity(String firstNamespace, String firstName, String secondNamespace,
            String secondName) {

        EntityType entityType = new EntityType();
        entityType.setNamespace(firstNamespace);
        entityType.setName(firstName);
        entityType = facade.create(entityType);

        Assert.assertNotNull(entityType.getId());
        Assert.assertEquals((Integer) 0, entityType.getVersion());

        EntityType updateEntity = new EntityType();
        updateEntity.setNamespace("secondNamespace");
        updateEntity.setName("secondName");
        updateEntity.setId(entityType.getId());
        updateEntity.setVersion(entityType.getVersion() + 1);

        EntityType entity1 = facade.update(updateEntity);

        List<EntityType> allEntities = facade.listAllEntities();
        EntityType entityFound = allEntities.get(0);

        Assert.assertEquals((Integer) 1, entity1.getVersion());
        Assert.assertNotSame(entityType, entityFound);
        facade.deleteEntity(entityType.getId());
    }

    public static void createAndVerifyTwoEntities(String entity1namespace, String entity1name, String entity2namespace,
            String entity2name) {
        EntityType entity1 = new EntityType();
        entity1.setNamespace(entity1namespace);
        entity1.setName(entity1name);
        entity1 = facade.create(entity1);

        EntityType entity2 = new EntityType();
        entity2.setNamespace(entity2namespace);
        entity2.setName(entity2name);
        entity2 = facade.create(entity2);

        Assert.assertNotNull(entity1.getId());
        Assert.assertNotNull(entity2.getId());

        Assert.assertEquals((Integer) 0, entity1.getVersion());
        Assert.assertEquals((Integer) 0, entity2.getVersion());

        List<EntityType> entityTypes = facade.listAllEntities();
        Assert.assertEquals(2, entityTypes.size());
        Assert.assertEquals(entity1, entityTypes.get(0));
        Assert.assertEquals(entity2, entityTypes.get(1));

        facade.deleteEntity(entity1.getId());
        facade.deleteEntity(entity2.getId());
    }

    public static EntityType createAndSaveOneEntity(String namespace, String name) {
        EntityType entityType = new EntityType();
        entityType.setNamespace(namespace);
        entityType.setName(name);
        entityType = facade.create(entityType);

        Assert.assertNotNull(entityType.getId());
        Assert.assertEquals((Integer) 0, entityType.getVersion());
        return entityType;
    }

    public static void createAndVerifyOneEntity(String namespace, String name) {
        EntityType entityType = new EntityType();
        entityType.setNamespace(namespace);
        entityType.setName(name);
        entityType = facade.create(entityType);

        Assert.assertNotNull(entityType.getId());
        Assert.assertEquals((Integer) 0, entityType.getVersion());

        List<EntityType> entityTypes = facade.listAllEntities();
        Assert.assertEquals(1, entityTypes.size());
        Assert.assertEquals(entityType, entityTypes.get(0));

        facade.deleteEntity(entityType.getId());
    }

    public static void setFacade(Facade facade) {
        EntityHelper.facade = facade;
    }

}
