package com.nanuvem.lom.api.tests;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.MetadataException;

public class EntityHelper {

	static Entity createEntity(Facade facade, String namespace, String name) {
		Entity entity = new Entity();
		entity.setName(name);
		entity.setNamespace(namespace);
		facade.create(entity);
		return entity;
	}

	static void expectExceptionOnInvalidFindEntityByFullName(Facade facade, String fullName,
			String expectedMessage) {
		try {
			facade.findEntityByFullName(fullName);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	static void expectExceptionOnInvalidEntityList(Facade facade, String fragment,
			String expectedMessage) {
		try {
			facade.listEntitiesByFullName(fragment);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	static void expectExceptionOnInvalidEntityUpdate(Facade facade, 
			String firstentitynamespace, String firstentityname,
			String secondentitynamespace, String secondentityname,
			String firstentitynamespaceupdate, String firstentitynameupdate,
			String expectedExceptionMessage) {
	
		Entity ea = new Entity();
		ea.setNamespace(firstentitynamespace);
		ea.setName(firstentityname);
		facade.create(ea);
	
		Entity eb = new Entity();
		eb.setNamespace(secondentitynamespace);
		eb.setName(secondentityname);
		facade.create(eb);
	
		try {
			facade.update(eb);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedExceptionMessage, me.getMessage());
			facade.deleteEntity(ea.getId());
			facade.deleteEntity(eb.getId());
		}
	}

	static void expectExceptionOnInvalidEntityUpdateUsingId(Facade facade, String namespace,
			String name, Long id, Integer version, String expectedMessage) {
		Entity updateEntity = new Entity();
		updateEntity.setNamespace(namespace);
		updateEntity.setName(name);
		updateEntity.setId(id);
		updateEntity.setVersion(version);
		try {
			facade.update(updateEntity);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	static void expectExceptionOnInvalidEntityUpdate(Facade facade, String firstnamespace,
			String firstname, String secondnamespace, String secondname,
			String expectedMessage) {
		Entity entity = new Entity();
		entity.setNamespace(firstnamespace);
		entity.setName(firstname);
		try {
			entity = facade.create(entity);
		} catch (MetadataException e) {
			fail(e.getMessage());
		}
	
		try {
			entity.setNamespace(secondnamespace);
			entity.setName(secondname);
			facade.update(entity);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	static void expectExceptionOnCreateInvalidEntity(Facade facade, String namespace,
			String name, String expectedMessage) {
		try {
			EntityHelper.createAndVerifyOneEntity(facade, namespace, name);
			fail();
		} catch (MetadataException e) {
			Assert.assertEquals(expectedMessage, e.getMessage());
		}
	}

	static void createUpdateAndVerifyOneEntity(Facade facade, String firstNamespace,
			String firstName, String updatePath, String secondNamespace,
			String secondName) {
	
		Entity entity = new Entity();
		entity.setNamespace(firstNamespace);
		entity.setName(firstName);
		facade.create(entity);
	
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
		facade.deleteEntity(entity1.getId());
	}

	static void createAndVerifyTwoEntities(Facade facade, String entity1namespace,
			String entity1name, String entity2namespace, String entity2name) {
		Entity entity1 = new Entity();
		entity1.setNamespace(entity1namespace);
		entity1.setName(entity1name);
		facade.create(entity1);
	
		Entity entity2 = new Entity();
		entity2.setNamespace(entity2namespace);
		entity2.setName(entity2name);
		facade.create(entity2);
	
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

	static Entity createAndSaveOneEntity(Facade facade, String namespace, String name) {
		Entity entity = new Entity();
		entity.setNamespace(namespace);
		entity.setName(name);
		facade.create(entity);
	
		Assert.assertNotNull(entity.getId());
		Assert.assertEquals((Integer) 0, entity.getVersion());
		return entity;
	}

	static void createAndVerifyOneEntity(Facade facade, String namespace, String name) {
		Entity entity = new Entity();
		entity.setNamespace(namespace);
		entity.setName(name);
		facade.create(entity);
	
		Assert.assertNotNull(entity.getId());
		Assert.assertEquals((Integer) 0, entity.getVersion());
	
		List<Entity> entities = facade.listAllEntities();
		Assert.assertEquals(1, entities.size());
		Assert.assertEquals(entity, entities.get(0));
	
		facade.deleteEntity(entity.getId());
	}

}
