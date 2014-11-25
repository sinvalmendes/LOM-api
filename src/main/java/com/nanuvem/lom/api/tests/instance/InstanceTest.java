package com.nanuvem.lom.api.tests.instance;

import org.junit.Test;

import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.tests.LomTestCase;

import static com.nanuvem.lom.api.tests.instance.InstanceHelper.*;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.*;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.*;

public abstract class InstanceTest extends LomTestCase {

    @Test
    public void unknownClass() {
        expectExceptionOnCreateInvalidInstance("a", "Entity not found: a", "30");

        expectExceptionOnCreateInvalidInstance("abc.a", "Entity not found: abc.a", "30");
    }

    @Test
    public void nullClass() {
        expectExceptionOnCreateInvalidInstance(null, "Invalid value for Instance entity: The entity is mandatory", "30");
    }

    @Test
    public void entityWithoutAttributes() {
        createEntity("abc", "a");
        createAndVerifyOneInstance("abc.a");

        createEntity("abc", "b");
        createAndVerifyOneInstance("abc.b");

        createEntity("", "a");
        createAndVerifyOneInstance("a");

        createEntity("", "b");
        createAndVerifyOneInstance("b");
    }

    @Test
    public void entityWithKnownAttributesAndWithoutConfiguration() {
        createEntity("system", "Client");
        createOneAttribute("system.Client", null, "pa", AttributeType.TEXT, null);

        createEntity("system", "User");
        createOneAttribute("system.User", null, "pa", AttributeType.TEXT, null);
        createOneAttribute("system.User", null, "pb", AttributeType.LONGTEXT, null);

        createEntity("system", "Organization");
        createOneAttribute("system.Organization", null, "pa", AttributeType.TEXT, null);
        createOneAttribute("system.Organization", null, "pb", AttributeType.LONGTEXT, null);
        createOneAttribute("system.Organization", null, "pc", AttributeType.INTEGER, null);

        createEntity("system", "Category");
        createOneAttribute("system.Category", null, "pa", AttributeType.TEXT, null);
        createOneAttribute("system.Category", null, "pb", AttributeType.LONGTEXT, null);
        createOneAttribute("system.Category", null, "pc", AttributeType.INTEGER, null);
        createOneAttribute("system.Category", null, "pd", AttributeType.PASSWORD, null);

        createAndVerifyOneInstance("system.client");
        createAndVerifyOneInstance("system.client", "va");
        createAndVerifyOneInstance("system.user");
        createAndVerifyOneInstance("system.user", "va", "vb");
        createAndVerifyOneInstance("system.organization");
        createAndVerifyOneInstance("system.organization", "va", "vb", "3");
        createAndVerifyOneInstance("system.category");
        createAndVerifyOneInstance("system.category", "va", "vb", "3", "vd");
    }

}