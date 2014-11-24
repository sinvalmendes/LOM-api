package com.nanuvem.lom.api.tests;

import static com.nanuvem.lom.api.tests.EntityHelper.createEntity;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.MetadataException;

public abstract class DeleteEntityServiceTest extends LomTestCase {

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
