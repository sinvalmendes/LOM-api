package com.nanuvem.lom.api.tests;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.MetadataException;

public abstract class EntityServiceTest {

	private Facade facade;

	public abstract Facade createFacade();

	@Before
	public void init() {
		facade = createFacade();
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
		this.expectExceptionOnCreateInvalidEntity("name space", "name",
				"Invalid value for Entity namespace: name space");
		this.expectExceptionOnCreateInvalidEntity("namespace", "na me",
				"Invalid value for Entity name: na me");
	}

	@Test
	public void withoutName() {
		this.expectExceptionOnCreateInvalidEntity("namespace", null,
				"The name of an Entity is mandatory");
		this.expectExceptionOnCreateInvalidEntity("namespace", "",
				"The name of an Entity is mandatory");
		this.expectExceptionOnCreateInvalidEntity(null, null,
				"The name of an Entity is mandatory");
		this.expectExceptionOnCreateInvalidEntity("", null,
				"The name of an Entity is mandatory");
	}

	@Test
	public void twoEntitiesWithSameNameInDefaultNamespace() {
		createAndSaveOneEntity(null, "aaa");
		this.expectExceptionOnCreateInvalidEntity(null, "aaa",
				"The aaa Entity already exists");
		this.expectExceptionOnCreateInvalidEntity("", "aaa",
				"The aaa Entity already exists");
	}

	@Test
	public void twoEntitiesWithSameNameInAnonDefaultNamespace() {
		createAndSaveOneEntity("a", "aaa");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa",
				"The a.aaa Entity already exists");
	}

	@Test
	public void nameAndNamespaceWithInvalidChars() {
		this.expectExceptionOnCreateInvalidEntity("a", "aaa$",
				"Invalid value for Entity name: aaa$");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa#",
				"Invalid value for Entity name: aaa#");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa=",
				"Invalid value for Entity name: aaa=");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa.a",
				"Invalid value for Entity name: aaa.a");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa/a",
				"Invalid value for Entity name: aaa/a");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa*",
				"Invalid value for Entity name: aaa*");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa'",
				"Invalid value for Entity name: aaa'");
		this.expectExceptionOnCreateInvalidEntity("a$", "aaa",
				"Invalid value for Entity namespace: a$");
		this.expectExceptionOnCreateInvalidEntity("a#", "aaa",
				"Invalid value for Entity namespace: a#");
		this.expectExceptionOnCreateInvalidEntity("a=", "aaa",
				"Invalid value for Entity namespace: a=");
		this.expectExceptionOnCreateInvalidEntity("a'", "aaa",
				"Invalid value for Entity namespace: a'");
		// this.expectExceptionOnCreateInvalidEntity("a.", "aaa",
		// "Invalid value for Entity namespace: a.");
		this.expectExceptionOnCreateInvalidEntity("a/a", "aaa",
				"Invalid value for Entity namespace: a/a");
		this.expectExceptionOnCreateInvalidEntity("a*", "aaa",
				"Invalid value for Entity namespace: a*");
	}

	@Test
	public void validNewNameAndPackage() {
		createUpdateAndVerifyOneEntity("a", "aaa1", "a.aaa1", "b", "bbb");
		createUpdateAndVerifyOneEntity("a", "aaa2", "a.aaa2", "a", "bbb");
		createUpdateAndVerifyOneEntity("a", "aaa3", "a.aaa3", "b", "aaa");
		createUpdateAndVerifyOneEntity("", "aaa1", "aaa1", "", "bbb");
		createUpdateAndVerifyOneEntity(null, "aaa2", "aaa2", null, "bbb");
		createUpdateAndVerifyOneEntity("a.b.c", "aaa1", "a.b.c.aaa1", "b", "bbb");
		createUpdateAndVerifyOneEntity("a.b.c", "aaa2", "a.b.c.aaa2", "b.c",
				"bbb");
	}

	@Test
	public void removePackageSetPackage() {
		createUpdateAndVerifyOneEntity("a", "aaa1", "a.aaa1", "", "aaa");
		createUpdateAndVerifyOneEntity("a", "aaa2", "a.aaa2", "", "bbb");
		createUpdateAndVerifyOneEntity("", "aaa1", "aaa1", "b", "bbb");
		createUpdateAndVerifyOneEntity("a", "aaa3", "a.aaa3", null, "aaa");
		createUpdateAndVerifyOneEntity("a", "aaa4", "a.aaa4", null, "bbb");
		createUpdateAndVerifyOneEntity(null, "aaa2", "aaa2", "b", "bbb");

		createUpdateAndVerifyOneEntity("a", "aaa5", "a.aaa5", "a", "aaa5");
		createUpdateAndVerifyOneEntity("a", "aaa6", "a.aaa6", "a", "aaa7");
		createUpdateAndVerifyOneEntity(null, "aaa3", "aaa3", null, "aaa4");
	}

	@Test
	public void renameCausingTwoEntitiesWithSameNameInDifferentPackages() {
		Entity ea = this.createAndSaveOneEntity("a", "aaa");
		this.createAndSaveOneEntity("b", "bbb");

		ea.setName("bbb");
		facade.update(ea);
	}

	@Test
	public void moveCausingTwoEntitiesWithSameNameInDifferentPackages() {
		Entity ea = new Entity();
		ea.setNamespace("a");
		ea.setName("aaa");
		facade.create(ea);

		Entity eb = new Entity();
		eb.setNamespace("b");
		eb.setName("bbb");
		facade.create(eb);

		ea.setNamespace("c");
		ea.setName("bbb");
		facade.update(ea);
	}

	@Test
	public void newNameAndPackageWithSpaces() {
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "name space", "aaa",
				"Invalid value for Entity namespace: name space");
		expectExceptionOnInvalidEntityUpdate("b", "bbb", "namespace", "na me",
				"Invalid value for Entity name: na me");
	}

	@Test
	public void removeName() {
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "namespace", null,
				"The name of an Entity is mandatory");
		expectExceptionOnInvalidEntityUpdate("a", "aaa2", "namespace", "",
				"The name of an Entity is mandatory");
		expectExceptionOnInvalidEntityUpdate("a", "aaa3", null, null,
				"The name of an Entity is mandatory");
		expectExceptionOnInvalidEntityUpdate("a", "aaa4", null, "",
				"The name of an Entity is mandatory");
	}

	@Test
	public void renameMoveCausingTwoEntitiesWithSameNameInDefaultPackage() {
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "b", "bbb", "b", "bbb",
				"The b.bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "b", "aaa", "b", "bbb",
				"The b.aaa Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "a", "bbb", "b", "bbb",
				"The a.bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a.b.c", "aaa", "b.c", "bbb",
				"b.c", "bbb", "The b.c.bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("b.c", "aaa", "b.c", "bbb", "b.c",
				"bbb", "The b.c.bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a.b.c", "bbb", "b.c", "bbb",
				"b.c", "bbb", "The b.c.bbb Entity already exists");
	}

	@Test
	public void renameMoveCausingTwoEntitiesWithSameNameInAnonDefaultPackage() {
		expectExceptionOnInvalidEntityUpdate("a", "aaa", null, "bbb", null,
				"bbb", "The bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate(null, "aaa", null, "bbb", null,
				"bbb", "The bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a", "bbb", null, "bbb", null,
				"bbb", "The bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a.b.c", "aaa", "", "bbb", "",
				"bbb", "The bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("", "aaa", "", "bbb", "", "bbb",
				"The bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a.b.c", "bbb", "", "bbb", "",
				"bbb", "The bbb Entity already exists");
	}

	@Test
	public void renameMoveCausingNameAndPackagesWithInvalidChars() {
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "a", "aaa$",
				"Invalid value for Entity name: aaa$");
		expectExceptionOnInvalidEntityUpdate("a", "aaa2", "a", "aaa#",
				"Invalid value for Entity name: aaa#");
		expectExceptionOnInvalidEntityUpdate("a", "aaa3", "a", "aaa=",
				"Invalid value for Entity name: aaa=");
		expectExceptionOnInvalidEntityUpdate("a", "aaa4", "a", "aaa'",
				"Invalid value for Entity name: aaa'");
		expectExceptionOnInvalidEntityUpdate("a", "aaa5", "a", "aaa.a",
				"Invalid value for Entity name: aaa.a");
		expectExceptionOnInvalidEntityUpdate("a", "aaa6", "a", "aaa/a",
				"Invalid value for Entity name: aaa/a");
		expectExceptionOnInvalidEntityUpdate("a", "aaa7", "a", "aaa*",
				"Invalid value for Entity name: aaa*");
		expectExceptionOnInvalidEntityUpdate("a", "aaa8", "a$", "aaa",
				"Invalid value for Entity namespace: a$");
		expectExceptionOnInvalidEntityUpdate("a", "aaa9", "a#", "aaa",
				"Invalid value for Entity namespace: a#");
		expectExceptionOnInvalidEntityUpdate("a", "aaab1", "a=", "aaa",
				"Invalid value for Entity namespace: a=");
		expectExceptionOnInvalidEntityUpdate("a", "aaab2", "a'", "aaa",
				"Invalid value for Entity namespace: a'");

		// this scenario of test it's wrong and delayed me ¬¬
		// expectExceptionOnInvalidEntityUpdate("a", "aaa", "a.", "aaa",
		// "Invalid value for Entity namespace: a.");
		expectExceptionOnInvalidEntityUpdate("a", "aaab3", "a/a", "aaa",
				"Invalid value for Entity namespace: a/a");
		expectExceptionOnInvalidEntityUpdate("a", "aaab4", "a*", "aaa",
				"Invalid value for Entity namespace: a*");
	}

	@Test
	public void renameMoveForcingCaseInsentivePackagesAndNames() {
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "b", "bbb", "b", "BbB",
				"The b.bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "b", "bbb", "b", "BBB",
				"The b.bbb Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "CcC", "ccc", "ccc",
				"ccc", "The CcC.ccc Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "CcC", "ccc", "CCC",
				"ccc", "The CcC.ccc Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "CCC", "ccc", "ccc",
				"ccc", "The CCC.ccc Entity already exists");
		expectExceptionOnInvalidEntityUpdate("a", "aaa", "CCC", "ccc", "ccc",
				"CCC", "The CCC.ccc Entity already exists");
	}

	@Test
	public void invalidIdAndVersion() {
		Entity entity1 = this.createAndSaveOneEntity("a", "aaa1");
		expectExceptionOnInvalidEntityUpdateUsingId("namespace", "name", null,
				entity1.getVersion(),
				"The id of an Entity is mandatory on update");

		Entity entity2 = this.createAndSaveOneEntity("a", "aaa2");
		expectExceptionOnInvalidEntityUpdateUsingId("a", "aaa2", entity2.getId(),
				null, "The version of an Entity is mandatory on update");

		this.createAndSaveOneEntity("a", "aaa");
		expectExceptionOnInvalidEntityUpdateUsingId("a", "aaa3", (Long) null,
				null, "The version and id of an Entity are mandatory on update");

		// the message of exception at this case was wrong and delayed me
		Entity entity4 = this.createAndSaveOneEntity("a", "aaa4");
		expectExceptionOnInvalidEntityUpdateUsingId("name", "aaa",
				entity4.getId() + 1, entity4.getVersion(),
				"Invalid id for Entity name.aaa");

		Entity entity5 = this.createAndSaveOneEntity("a", "aaa5");
		expectExceptionOnInvalidEntityUpdateUsingId(
				"namespace",
				"name",
				entity5.getId(),
				entity5.getVersion() - 1,
				"Updating a deprecated version of Entity a.aaa5. Get the Entity again to obtain the newest version and proceed updating.");
	}

	@Test
	public void severalUpdates() {
		Entity ea = new Entity();
		ea.setNamespace("a");
		ea.setName("aaa");
		ea = facade.create(ea);

		ea.setNamespace("b");
		ea.setName("abc");
		ea.setVersion(ea.getVersion() + 1);
		ea = facade.update(ea);

		ea.setNamespace("a.b.d");
		ea.setName("abc");
		ea.setVersion(ea.getVersion() + 1);
		ea = facade.update(ea);

		ea.setNamespace(null);
		ea.setName("abc");
		ea.setVersion(ea.getVersion() + 1);
		ea = facade.update(ea);

		ea.setNamespace("a.b.c");
		ea.setName("abc");
		ea.setVersion(ea.getVersion() + 1);
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

		Entity entity1 = this.createAndSaveOneEntity("ns1", "n1");
		Entity entity2 = this.createAndSaveOneEntity("ns2", "n2");
		Entity entity3 = this.createAndSaveOneEntity("ns2", "n3");

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

		List<Entity> allEntities = facade
				.listEntitiesByFullName(namespaceFragment);
		Assert.assertEquals(0, allEntities.size());

		allEntities = facade.listEntitiesByFullName(nameFragment);
		Assert.assertEquals(0, allEntities.size());

		Entity entity1 = this.createAndSaveOneEntity("ns1", "n1");
		Entity entity2 = this.createAndSaveOneEntity("ns2", "n2");
		Entity entity3 = this.createAndSaveOneEntity("ns2", "n3");

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

		Entity entity1 = this.createAndSaveOneEntity("ns1", "n1");
		Entity entity2 = this.createAndSaveOneEntity("ns2", "n2");
		Entity entity3 = this.createAndSaveOneEntity("ns2", "n3");

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
		expectExceptionOnInvalidEntityList("na me",
				"Invalid value for Entity full name: na me");
	}

	@Test
	public void listEntitiesForcingCaseInsensitivePackagesAndNames() {
		Entity entity1 = this.createAndSaveOneEntity("ns1", "n1");
		Entity entity2 = this.createAndSaveOneEntity("NS2", "n2");
		Entity entity3 = this.createAndSaveOneEntity("NS3", "N3");
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
		expectExceptionOnInvalidEntityList("n$",
				"Invalid value for Entity full name: n$");
		expectExceptionOnInvalidEntityList("n#",
				"Invalid value for Entity full name: n#");
		expectExceptionOnInvalidEntityList("n=",
				"Invalid value for Entity full name: n=");
		expectExceptionOnInvalidEntityList("n'",
				"Invalid value for Entity full name: n'");
		expectExceptionOnInvalidEntityList("n/n",
				"Invalid value for Entity full name: n/n");
		expectExceptionOnInvalidEntityList("n*",
				"Invalid value for Entity full name: n*");
	}

	@Test
	public void getEntityByValidNameAndPackage() {
		expectExceptionOnInvalidFindEntityByFullName("ns.n", "Entity not found: ns.n");

		Entity entity1 = createEntity("ns1", "n1");
		Entity foundEntity1 = facade.findEntityByFullName("ns1.n1");
		Assert.assertEquals(entity1, foundEntity1);

		Entity entity2 = createEntity("ns2", "n2");
		Entity foundEntity2 = facade.findEntityByFullName("ns2.n2");
		Assert.assertEquals(entity2, foundEntity2);

		expectExceptionOnInvalidFindEntityByFullName("ns1.n", "Entity not found: ns1.n");
		expectExceptionOnInvalidFindEntityByFullName("ns.n1", "Entity not found: ns.n1");
		expectExceptionOnInvalidFindEntityByFullName("ns2.n1", "Entity not found: ns2.n1");

		List<Entity> allEntities = facade.listAllEntities();
		Assert.assertEquals(2, allEntities.size());
		Assert.assertEquals(entity1, allEntities.get(0));
		Assert.assertEquals(entity2, allEntities.get(1));
	}

	@Test
	public void getEntityByEmptyNameAndPackage() {
		createEntity("ns1", "n1");
		Entity entity2 = createEntity(null, "n2");
		expectExceptionOnInvalidFindEntityByFullName(".n1", "Entity not found: n1");

		Entity foundEntity2 = facade.findEntityByFullName(".n2");
		Assert.assertEquals(entity2, foundEntity2);
		expectExceptionOnInvalidFindEntityByFullName("ns1.", "Entity not found: ns1");
	}

	@Test
	public void getEntityByNameAndPackageWithSpaces() {
		expectExceptionOnInvalidFindEntityByFullName(".na me",
				"Invalid key for Entity: na me");
		expectExceptionOnInvalidFindEntityByFullName("name space.name",
				"Invalid key for Entity: name space.name");
		expectExceptionOnInvalidFindEntityByFullName("namespace.na me",
				"Invalid key for Entity: namespace.na me");
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
		expectExceptionOnInvalidFindEntityByFullName("ns.n$",
				"Invalid key for Entity: ns.n$");
		expectExceptionOnInvalidFindEntityByFullName("ns.n#",
				"Invalid key for Entity: ns.n#");
		expectExceptionOnInvalidFindEntityByFullName("ns.n=",
				"Invalid key for Entity: ns.n=");
		expectExceptionOnInvalidFindEntityByFullName("ns.n/n",
				"Invalid key for Entity: ns.n/n");
		expectExceptionOnInvalidFindEntityByFullName("ns.n*",
				"Invalid key for Entity: ns.n*");
		expectExceptionOnInvalidFindEntityByFullName("ns.n'",
				"Invalid key for Entity: ns.n'");
		expectExceptionOnInvalidFindEntityByFullName("ns$.n",
				"Invalid key for Entity: ns$.n");
		expectExceptionOnInvalidFindEntityByFullName("ns#.n",
				"Invalid key for Entity: ns#.n");
		expectExceptionOnInvalidFindEntityByFullName("ns=.n",
				"Invalid key for Entity: ns=.n");
		expectExceptionOnInvalidFindEntityByFullName("ns/.n",
				"Invalid key for Entity: ns/.n");
		expectExceptionOnInvalidFindEntityByFullName("ns*.n",
				"Invalid key for Entity: ns*.n");
		expectExceptionOnInvalidFindEntityByFullName("ns'.n",
				"Invalid key for Entity: ns'.n");
	}

	@Test
	public void deleteEntityForcingCaseInsensitivePackagesAndNames() {
		Entity c = this.createEntity("nS", "nA");
		c.setNamespace("ns");
		c.setName("na");
		facade.deleteEntity(c.getId());

		c = this.createEntity("nS", "nA");
		c.setNamespace("NS");
		c.setName("NA");
		facade.deleteEntity(c.getId());

		c = this.createEntity("nS", "nA");
		facade.deleteEntity(c.getId());

		c = this.createEntity("nS", "nA");
		c.setNamespace("NS");
		c.setName("na");
		facade.deleteEntity(c.getId());

		c = this.createEntity("nS", "nA");
		c.setNamespace("ns");
		facade.deleteEntity(c.getId());

		c = this.createEntity("nS", "nA");
		c.setNamespace("Ns");
		c.setName("Na");
		facade.deleteEntity(c.getId());
	}

	private Entity createEntity(String namespace, String name) {
		Entity entity = new Entity();
		entity.setName(name);
		entity.setNamespace(namespace);
		facade.create(entity);
		return entity;
	}

	private void expectExceptionOnInvalidFindEntityByFullName(String fullName,
			String expectedMessage) {
		try {
			facade.findEntityByFullName(fullName);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	private void expectExceptionOnInvalidEntityList(String fragment,
			String expectedMessage) {
		try {
			facade.listEntitiesByFullName(fragment);
			fail();
		} catch (MetadataException me) {
			Assert.assertEquals(expectedMessage, me.getMessage());
		}
	}

	private void expectExceptionOnInvalidEntityUpdate(
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

	private void expectExceptionOnInvalidEntityUpdateUsingId(String namespace,
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

	private void expectExceptionOnInvalidEntityUpdate(String firstnamespace,
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

	private void expectExceptionOnCreateInvalidEntity(String namespace,
			String name, String expectedMessage) {
		try {
			createAndVerifyOneEntity(namespace, name);
			fail();
		} catch (MetadataException e) {
			Assert.assertEquals(expectedMessage, e.getMessage());
		}
	}

	private void createUpdateAndVerifyOneEntity(String firstNamespace,
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

	private void createAndVerifyTwoEntities(String entity1namespace,
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

	private Entity createAndSaveOneEntity(String namespace, String name) {
		Entity entity = new Entity();
		entity.setNamespace(namespace);
		entity.setName(name);
		facade.create(entity);

		Assert.assertNotNull(entity.getId());
		Assert.assertEquals((Integer) 0, entity.getVersion());
		return entity;
	}

	private void createAndVerifyOneEntity(String namespace, String name) {
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
