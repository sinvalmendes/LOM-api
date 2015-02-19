package com.nanuvem.lom.api.tests.entity;

import static com.nanuvem.lom.api.tests.entity.EntityHelper.createEntity;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.tests.LomTestCase;

public abstract class DeleteEntityTest extends LomTestCase {

	@Test
	public void deleteEntity() {
		Entity c = createEntity("a", "aaa");
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
