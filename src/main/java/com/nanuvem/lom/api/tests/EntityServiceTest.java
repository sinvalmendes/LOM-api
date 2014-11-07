package com.nanuvem.lom.api.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;

public abstract class EntityServiceTest {

	private Facade facade;

	public abstract Facade createFacade();

	@Before
	public void init() {
		facade = createFacade();
	}
	
	@Test
	public void validNameAndNamespace() {
		EntityHelper.createAndVerifyOneEntity(facade, "abc", "abc");
		EntityHelper.createAndVerifyOneEntity(facade, "a.b.c", "abc");
		EntityHelper.createAndVerifyOneEntity(facade, "a", "a");
		EntityHelper.createAndVerifyOneEntity(facade, "abc123", "aaa");
		EntityHelper.createAndVerifyOneEntity(facade, "abc", "abc1122");
	}

	@Test
	public void withoutNamespace() {
		EntityHelper.createAndVerifyOneEntity(facade, "", "abc");
		EntityHelper.createAndVerifyOneEntity(facade, null, "abc");
		EntityHelper.createAndVerifyOneEntity(facade, "", "a1");
		EntityHelper.createAndVerifyOneEntity(facade, null, "a1");
	}

	@Test
	public void twoEntitiesWithSameNameInDifferentNamespaces() {
		EntityHelper.createAndVerifyTwoEntities(facade, "p1", "name", "p2", "name");
		EntityHelper.createAndVerifyTwoEntities(facade, null, "name", "p1", "name");
		EntityHelper.createAndVerifyTwoEntities(facade, "a", "name", "a.b", "name");
	}

	@Test
	public void nameAndNamespaceWithSpaces() {
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "name space", "name",
				"Invalid value for Entity namespace: name space");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "namespace", "na me",
				"Invalid value for Entity name: na me");
	}

	@Test
	public void withoutName() {
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "namespace", null,
				"The name of an Entity is mandatory");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "namespace", "",
				"The name of an Entity is mandatory");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, null, null,
				"The name of an Entity is mandatory");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "", null,
				"The name of an Entity is mandatory");
	}

	@Test
	public void twoEntitiesWithSameNameInDefaultNamespace() {
		EntityHelper.createAndSaveOneEntity(facade, null, "aaa");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, null, "aaa",
				"The aaa Entity already exists");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "", "aaa",
				"The aaa Entity already exists");
	}

	@Test
	public void twoEntitiesWithSameNameInAnonDefaultNamespace() {
		EntityHelper.createAndSaveOneEntity(facade, "a", "aaa");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a", "aaa",
				"The a.aaa Entity already exists");
	}

	@Test
	public void nameAndNamespaceWithInvalidChars() {
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a", "aaa$",
				"Invalid value for Entity name: aaa$");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a", "aaa#",
				"Invalid value for Entity name: aaa#");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a", "aaa=",
				"Invalid value for Entity name: aaa=");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a", "aaa.a",
				"Invalid value for Entity name: aaa.a");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a", "aaa/a",
				"Invalid value for Entity name: aaa/a");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a", "aaa*",
				"Invalid value for Entity name: aaa*");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a", "aaa'",
				"Invalid value for Entity name: aaa'");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a$", "aaa",
				"Invalid value for Entity namespace: a$");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a#", "aaa",
				"Invalid value for Entity namespace: a#");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a=", "aaa",
				"Invalid value for Entity namespace: a=");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a'", "aaa",
				"Invalid value for Entity namespace: a'");
		// expectExceptionOnCreateInvalidEntity(facade, "a.", "aaa",
		// "Invalid value for Entity namespace: a.");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a/a", "aaa",
				"Invalid value for Entity namespace: a/a");
		EntityHelper.expectExceptionOnCreateInvalidEntity(facade, "a*", "aaa",
				"Invalid value for Entity namespace: a*");
	}

	@Test
	public void validNewNameAndPackage() {
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa1", "a.aaa1", "b", "bbb");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa2", "a.aaa2", "a", "bbb");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa3", "a.aaa3", "b", "aaa");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "", "aaa1", "aaa1", "", "bbb");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, null, "aaa2", "aaa2", null, "bbb");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a.b.c", "aaa1", "a.b.c.aaa1", "b", "bbb");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a.b.c", "aaa2", "a.b.c.aaa2", "b.c",
				"bbb");
	}

	@Test
	public void removePackageSetPackage() {
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa1", "a.aaa1", "", "aaa");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa2", "a.aaa2", "", "bbb");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "", "aaa1", "aaa1", "b", "bbb");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa3", "a.aaa3", null, "aaa");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa4", "a.aaa4", null, "bbb");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, null, "aaa2", "aaa2", "b", "bbb");

		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa5", "a.aaa5", "a", "aaa5");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, "a", "aaa6", "a.aaa6", "a", "aaa7");
		EntityHelper.createUpdateAndVerifyOneEntity(facade, null, "aaa3", "aaa3", null, "aaa4");
	}

	@Test
	public void renameCausingTwoEntitiesWithSameNameInDifferentPackages() {
		Entity ea = EntityHelper.createAndSaveOneEntity(facade, "a", "aaa");
		EntityHelper.createAndSaveOneEntity(facade, "b", "bbb");

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
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "name space", "aaa",
				"Invalid value for Entity namespace: name space");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "b", "bbb", "namespace", "na me",
				"Invalid value for Entity name: na me");
	}

	@Test
	public void removeName() {
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "namespace", null,
				"The name of an Entity is mandatory");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa2", "namespace", "",
				"The name of an Entity is mandatory");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa3", null, null,
				"The name of an Entity is mandatory");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa4", null, "",
				"The name of an Entity is mandatory");
	}

	@Test
	public void renameMoveCausingTwoEntitiesWithSameNameInDefaultPackage() {
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "b", "bbb", "b", "bbb",
				"The b.bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "b", "aaa", "b", "bbb",
				"The b.aaa Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "a", "bbb", "b", "bbb",
				"The a.bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a.b.c", "aaa", "b.c", "bbb",
				"b.c", "bbb", "The b.c.bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "b.c", "aaa", "b.c", "bbb", "b.c",
				"bbb", "The b.c.bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a.b.c", "bbb", "b.c", "bbb",
				"b.c", "bbb", "The b.c.bbb Entity already exists");
	}

	@Test
	public void renameMoveCausingTwoEntitiesWithSameNameInAnonDefaultPackage() {
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", null, "bbb", null,
				"bbb", "The bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, null, "aaa", null, "bbb", null,
				"bbb", "The bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "bbb", null, "bbb", null,
				"bbb", "The bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a.b.c", "aaa", "", "bbb", "",
				"bbb", "The bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "", "aaa", "", "bbb", "", "bbb",
				"The bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a.b.c", "bbb", "", "bbb", "",
				"bbb", "The bbb Entity already exists");
	}

	@Test
	public void renameMoveCausingNameAndPackagesWithInvalidChars() {
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "a", "aaa$",
				"Invalid value for Entity name: aaa$");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa2", "a", "aaa#",
				"Invalid value for Entity name: aaa#");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa3", "a", "aaa=",
				"Invalid value for Entity name: aaa=");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa4", "a", "aaa'",
				"Invalid value for Entity name: aaa'");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa5", "a", "aaa.a",
				"Invalid value for Entity name: aaa.a");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa6", "a", "aaa/a",
				"Invalid value for Entity name: aaa/a");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa7", "a", "aaa*",
				"Invalid value for Entity name: aaa*");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa8", "a$", "aaa",
				"Invalid value for Entity namespace: a$");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa9", "a#", "aaa",
				"Invalid value for Entity namespace: a#");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaab1", "a=", "aaa",
				"Invalid value for Entity namespace: a=");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaab2", "a'", "aaa",
				"Invalid value for Entity namespace: a'");

		// this scenario of test it's wrong and delayed me ¬¬
		// expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "a.", "aaa",
		// "Invalid value for Entity namespace: a.");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaab3", "a/a", "aaa",
				"Invalid value for Entity namespace: a/a");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaab4", "a*", "aaa",
				"Invalid value for Entity namespace: a*");
	}

	@Test
	public void renameMoveForcingCaseInsentivePackagesAndNames() {
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "b", "bbb", "b", "BbB",
				"The b.bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "b", "bbb", "b", "BBB",
				"The b.bbb Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "CcC", "ccc", "ccc",
				"ccc", "The CcC.ccc Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "CcC", "ccc", "CCC",
				"ccc", "The CcC.ccc Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "CCC", "ccc", "ccc",
				"ccc", "The CCC.ccc Entity already exists");
		EntityHelper.expectExceptionOnInvalidEntityUpdate(facade, "a", "aaa", "CCC", "ccc", "ccc",
				"CCC", "The CCC.ccc Entity already exists");
	}

	@Test
	public void invalidIdAndVersion() {
		Entity entity1 = EntityHelper.createAndSaveOneEntity(facade, "a", "aaa1");
		EntityHelper.expectExceptionOnInvalidEntityUpdateUsingId(facade, "namespace", "name", null,
				entity1.getVersion(),
				"The id of an Entity is mandatory on update");

		Entity entity2 = EntityHelper.createAndSaveOneEntity(facade, "a", "aaa2");
		EntityHelper.expectExceptionOnInvalidEntityUpdateUsingId(facade, "a", "aaa2", entity2.getId(),
				null, "The version of an Entity is mandatory on update");

		EntityHelper.createAndSaveOneEntity(facade, "a", "aaa");
		EntityHelper.expectExceptionOnInvalidEntityUpdateUsingId(facade, "a", "aaa3", (Long) null,
				null, "The version and id of an Entity are mandatory on update");

		// the message of exception at this case was wrong and delayed me
		Entity entity4 = EntityHelper.createAndSaveOneEntity(facade, "a", "aaa4");
		EntityHelper.expectExceptionOnInvalidEntityUpdateUsingId(facade, "name", "aaa",
				entity4.getId() + 1, entity4.getVersion(),
				"Invalid id for Entity name.aaa");

		Entity entity5 = EntityHelper.createAndSaveOneEntity(facade, "a", "aaa5");
		EntityHelper.expectExceptionOnInvalidEntityUpdateUsingId(facade, 
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

		Entity entity1 = EntityHelper.createAndSaveOneEntity(facade, "ns1", "n1");
		Entity entity2 = EntityHelper.createAndSaveOneEntity(facade, "ns2", "n2");
		Entity entity3 = EntityHelper.createAndSaveOneEntity(facade, "ns2", "n3");

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

		Entity entity1 = EntityHelper.createAndSaveOneEntity(facade, "ns1", "n1");
		Entity entity2 = EntityHelper.createAndSaveOneEntity(facade, "ns2", "n2");
		Entity entity3 = EntityHelper.createAndSaveOneEntity(facade, "ns2", "n3");

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

		Entity entity1 = EntityHelper.createAndSaveOneEntity(facade, "ns1", "n1");
		Entity entity2 = EntityHelper.createAndSaveOneEntity(facade, "ns2", "n2");
		Entity entity3 = EntityHelper.createAndSaveOneEntity(facade, "ns2", "n3");

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
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "na me",
				"Invalid value for Entity full name: na me");
	}

	@Test
	public void listEntitiesForcingCaseInsensitivePackagesAndNames() {
		Entity entity1 = EntityHelper.createAndSaveOneEntity(facade, "ns1", "n1");
		Entity entity2 = EntityHelper.createAndSaveOneEntity(facade, "NS2", "n2");
		Entity entity3 = EntityHelper.createAndSaveOneEntity(facade, "NS3", "N3");
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
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "n$",
				"Invalid value for Entity full name: n$");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "n#",
				"Invalid value for Entity full name: n#");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "n=",
				"Invalid value for Entity full name: n=");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "n'",
				"Invalid value for Entity full name: n'");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "n/n",
				"Invalid value for Entity full name: n/n");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "n*",
				"Invalid value for Entity full name: n*");
	}

	@Test
	public void getEntityByValidNameAndPackage() {
		Assert.assertEquals(0, facade.listEntitiesByFullName("ns.n").size());

		Entity entity1 = EntityHelper.createEntity(facade, "ns1", "n1");
		Entity foundEntity1 = facade.findEntityByFullName("ns1.n1");
		Assert.assertEquals(entity1, foundEntity1);

		Entity entity2 = EntityHelper.createEntity(facade, "ns2", "n2");
		Entity foundEntity2 = facade.findEntityByFullName("ns2.n2");
		Assert.assertEquals(entity2, foundEntity2);

		Assert.assertEquals(0, facade.listEntitiesByFullName("ns1.n").size());
		Assert.assertEquals(0, facade.listEntitiesByFullName("ns.n1").size());
		Assert.assertEquals(0, facade.listEntitiesByFullName("ns2.n1").size());

		List<Entity> allEntities = facade.listAllEntities();
		Assert.assertEquals(2, allEntities.size());
		Assert.assertEquals(entity1, allEntities.get(0));
		Assert.assertEquals(entity2, allEntities.get(1));
	}

	@Test
	public void getEntityByEmptyNameAndPackage() {
		EntityHelper.createEntity(facade, "ns1", "n1");
		Entity entity2 = EntityHelper.createEntity(facade, null, "n2");
		Assert.assertEquals(0, facade.listEntitiesByFullName(".n1").size());

		Entity foundEntity2 = facade.findEntityByFullName(".n2");
		Assert.assertEquals(entity2, foundEntity2);
		Assert.assertEquals(0, facade.listEntitiesByFullName("ns1.").size());
	}

	@Test
	public void getEntityByNameAndPackageWithSpaces() {
		EntityHelper.expectExceptionOnInvalidEntityList(facade, ".na me",
				"Invalid value for Entity full name: .na me");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "name space.name",
				"Invalid value for Entity full name: name space.name");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "namespace.na me",
				"Invalid value for Entity full name: namespace.na me");
	}

	@Test
	public void getEntityForcingCaseInsensitivePackagesAndNames() {
		Entity entity = EntityHelper.createEntity(facade, "nS", "nA");
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
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns.n$",
				"Invalid value for Entity full name: ns.n$");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns.n#",
				"Invalid value for Entity full name: ns.n#");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns.n=",
				"Invalid value for Entity full name: ns.n=");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns.n/n",
				"Invalid value for Entity full name: ns.n/n");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns.n*",
				"Invalid value for Entity full name: ns.n*");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns.n'",
				"Invalid value for Entity full name: ns.n'");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns$.n",
				"Invalid value for Entity full name: ns$.n");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns#.n",
				"Invalid value for Entity full name: ns#.n");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns=.n",
				"Invalid value for Entity full name: ns=.n");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns/.n",
				"Invalid value for Entity full name: ns/.n");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns*.n",
				"Invalid value for Entity full name: ns*.n");
		EntityHelper.expectExceptionOnInvalidEntityList(facade, "ns'.n",
				"Invalid value for Entity full name: ns'.n");
	}

	@Test
	public void deleteEntityForcingCaseInsensitivePackagesAndNames() {
		Entity c = EntityHelper.createEntity(facade, "nS", "nA");
		c.setNamespace("ns");
		c.setName("na");
		facade.deleteEntity(c.getId());

		c = EntityHelper.createEntity(facade, "nS", "nA");
		c.setNamespace("NS");
		c.setName("NA");
		facade.deleteEntity(c.getId());

		c = EntityHelper.createEntity(facade, "nS", "nA");
		facade.deleteEntity(c.getId());

		c = EntityHelper.createEntity(facade, "nS", "nA");
		c.setNamespace("NS");
		c.setName("na");
		facade.deleteEntity(c.getId());

		c = EntityHelper.createEntity(facade, "nS", "nA");
		c.setNamespace("ns");
		facade.deleteEntity(c.getId());

		c = EntityHelper.createEntity(facade, "nS", "nA");
		c.setNamespace("Ns");
		c.setName("Na");
		facade.deleteEntity(c.getId());
	}
}
