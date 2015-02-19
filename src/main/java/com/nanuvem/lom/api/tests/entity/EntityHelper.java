package com.nanuvem.lom.api.tests.entity;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.MetadataException;

public class EntityHelper {

	private static Facade facade;

	public static Entity createEntity(String namespace, String name) {
		Entity entity = new Entity();
		entity.setName(name);
		entity.setNamespace(namespace);
		entity = facade.create(entity);
		return entity;
	}

	public static void expectExceptionOnInvalidFindEntityByFullName(
			String fullName, String expectedMessage) {
		try {
			facade.findEntityByFullName(fullName);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	public static void expectExceptionOnInvalidEntityList(String fragment,
			String expectedMessage, String... args) {
		try {
			facade.listEntitiesByFullName(fragment);
			fail();
		} catch (MetadataException e) {
			String formatedMessage = String.format(expectedMessage,
					(Object[]) args);
			Assert.assertEquals(formatedMessage, e.getMessage());
		}
	}

	public static void expectExceptionOnInvalidEntityUpdate(Entity entity,
			String secondnamespace, String secondname, String expectedMessage,
			String... args) {

		try {
			entity.setNamespace(secondnamespace);
			entity.setName(secondname);
			facade.update(entity);
			fail();
		} catch (MetadataException e) {
			String formatedMessage = String.format(expectedMessage,
					(Object[]) args);
			Assert.assertEquals(formatedMessage, e.getMessage());
		}
	}

	public static void expectExceptionOnCreateInvalidEntity(String namespace,
			String name, String expectedMessage, String... args) {
		try {
			createAndVerifyOneEntity(namespace, name);
			fail();
		} catch (MetadataException e) {
			String formatedMessage = String.format(expectedMessage,
					(Object[]) args);
			Assert.assertEquals(formatedMessage, e.getMessage());
		}
	}

	public static void createUpdateAndVerifyOneEntity(String firstNamespace,
			String firstName, String secondNamespace, String secondName) {

		Entity entity = new Entity();
		entity.setNamespace(firstNamespace);
		entity.setName(firstName);
		entity = facade.create(entity);

		Assert.assertNotNull(entity.getId());
		Assert.assertEquals((Integer) 0, entity.getVersion());

		Entity updateEntity = new Entity();
		updateEntity.setNamespace("secondNamespace");
		updateEntity.setName("secondName");
		updateEntity.setId(entity.getId());
		updateEntity.setVersion(entity.getVersion() + 1);

		Entity entity1 = facade.update(updateEntity);

		List<Entity> allEntities = facade.listAllEntities();
		Entity entityFound = allEntities.get(0);

		Assert.assertEquals((Integer) 1, entity1.getVersion());
		Assert.assertNotSame(entity, entityFound);
		facade.deleteEntity(entity.getId());
	}

	public static void createAndVerifyTwoEntities(String entity1namespace,
			String entity1name, String entity2namespace, String entity2name) {
		Entity entity1 = new Entity();
		entity1.setNamespace(entity1namespace);
		entity1.setName(entity1name);
		entity1 = facade.create(entity1);

		Entity entity2 = new Entity();
		entity2.setNamespace(entity2namespace);
		entity2.setName(entity2name);
		entity2 = facade.create(entity2);

		Assert.assertNotNull(entity1.getId());
		Assert.assertNotNull(entity2.getId());

		Assert.assertEquals((Integer) 0, entity1.getVersion());
		Assert.assertEquals((Integer) 0, entity2.getVersion());

		List<Entity> entities = facade.listAllEntities();
		Assert.assertEquals(2, entities.size());
		Assert.assertEquals(entity1, entities.get(0));
		Assert.assertEquals(entity2, entities.get(1));

		facade.deleteEntity(entity1.getId());
		facade.deleteEntity(entity2.getId());
	}

	public static Entity createAndSaveOneEntity(String namespace, String name) {
		Entity entity = new Entity();
		entity.setNamespace(namespace);
		entity.setName(name);
		entity = facade.create(entity);

		Assert.assertNotNull(entity.getId());
		Assert.assertEquals((Integer) 0, entity.getVersion());
		return entity;
	}

	public static void createAndVerifyOneEntity(String namespace, String name) {
		Entity entity = new Entity();
		entity.setNamespace(namespace);
		entity.setName(name);
		entity = facade.create(entity);

		Assert.assertNotNull(entity.getId());
		Assert.assertEquals((Integer) 0, entity.getVersion());

		List<Entity> entities = facade.listAllEntities();
		Assert.assertEquals(1, entities.size());
		Assert.assertEquals(entity, entities.get(0));

		facade.deleteEntity(entity.getId());
	}

	public static void setFacade(Facade facade) {
		EntityHelper.facade = facade;
	}

}
