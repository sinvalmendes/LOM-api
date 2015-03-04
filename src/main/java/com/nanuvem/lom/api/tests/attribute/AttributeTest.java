package com.nanuvem.lom.api.tests.attribute;

import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.*;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.createEntity;
import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.PropertyType;
import com.nanuvem.lom.api.tests.LomTestCase;

public abstract class AttributeTest extends LomTestCase {

    @Test
    public void validAttributeData() {
        createEntity("abc", "a");
        createAndVerifyOneAttribute("abc.a", 1, "pa", TEXT, MANDATORY_TRUE);
        createAndVerifyOneAttribute("abc.a", 1, "pe", LONGTEXT, MANDATORY_FALSE);
        createAndVerifyOneAttribute("abc.a", 1, "pi", TEXT, "");
        createAndVerifyOneAttribute("abc.a", 1, "po", TEXT, null);
    }

    @Test
    public void invalidAttributeData() {
        createEntity("abc", "a");
        expectExceptionOnCreateInvalidAttribute("abc.a", 0, "pa", TEXT, MANDATORY_TRUE, INVALID_SEQUENCE, "0");
        expectExceptionOnCreateInvalidAttribute("abc.a", -1, "pa", TEXT, MANDATORY_TRUE, INVALID_SEQUENCE, "-1");
        expectExceptionOnCreateInvalidAttribute("abc.a", 2, "pa", TEXT, MANDATORY_TRUE, INVALID_SEQUENCE, "2");
        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "", TEXT, MANDATORY_TRUE, ATTRIBUTE_IS_MANDATORY, "name");
        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", null, MANDATORY_TRUE, ATTRIBUTE_IS_MANDATORY, "type");
        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", TEXT, "ABC",
                "Invalid value for Attribute configuration: ABC");
    }

    @Test
    public void invalidAttributeName() {
        createEntity("abc", "a");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa$", TEXT, MANDATORY_TRUE, INVALID_VALUE_FOR_ATTRIBUTE,
                "name", "aaa$");
        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa#", TEXT, MANDATORY_TRUE, INVALID_VALUE_FOR_ATTRIBUTE,
                "name", "aaa#");
        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa=", TEXT, MANDATORY_TRUE, INVALID_VALUE_FOR_ATTRIBUTE,
                "name", "aaa=");
        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa'", TEXT, MANDATORY_TRUE, INVALID_VALUE_FOR_ATTRIBUTE,
                "name", "aaa'");
        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa.a", TEXT, MANDATORY_TRUE, INVALID_VALUE_FOR_ATTRIBUTE,
                "name", "aaa.a");
        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa*", TEXT, MANDATORY_TRUE, INVALID_VALUE_FOR_ATTRIBUTE,
                "name", "aaa*");
    }

    @Test
    public void validateSeveralAttributesInTheSameEntity() {
        createEntity("abc", "a");

        createAndVerifyOneAttribute("abc.a", null, "pa", TEXT, "");
        Assert.assertEquals(new Integer(1), facade.findAttributeByNameAndEntityFullName("pa", "abc.a").getSequence());

        createAndVerifyOneAttribute("abc.a", null, "pb", LONGTEXT, "");
        Assert.assertEquals(new Integer(2), facade.findAttributeByNameAndEntityFullName("pb", "abc.a").getSequence());

        createEntity("", "b");

        createAndVerifyOneAttribute("b", new Integer(1), "pa", LONGTEXT, "");
        createAndVerifyOneAttribute("b", new Integer(1), "pb", LONGTEXT, "");
        Assert.assertEquals(new Integer(2), facade.findAttributeByNameAndEntityFullName("pa", "b").getSequence());
        Assert.assertEquals(new Integer(1), facade.findAttributeByNameAndEntityFullName("pb", "b").getSequence());

        createEntity("", "c");

        createAndVerifyOneAttribute("c", new Integer(1), "pa", TEXT, "");
        createAndVerifyOneAttribute("c", new Integer(2), "pb", LONGTEXT, "");
        createAndVerifyOneAttribute("c", new Integer(2), "pc", LONGTEXT, "");
        Assert.assertEquals(new Integer(1), facade.findAttributeByNameAndEntityFullName("pa", "c").getSequence());
        Assert.assertEquals(new Integer(3), facade.findAttributeByNameAndEntityFullName("pb", "c").getSequence());
        Assert.assertEquals(new Integer(2), facade.findAttributeByNameAndEntityFullName("pc", "c").getSequence());
    }

    @Test
    public void validateAttributeDuplicationInTheSameEntity() {
        createEntity("abc", "a");
        createAndVerifyOneAttribute("abc.a", null, "pa", TEXT, "");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", LONGTEXT, "", ATTRIBUTE_DUPLICATION, "abc.a", "pa");
        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "", ATTRIBUTE_DUPLICATION, "abc.a", "pa");
        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pA", TEXT, "", ATTRIBUTE_DUPLICATION, "abc.a", "pa");
        expectExceptionOnCreateInvalidAttribute("abc.a", null, "PA", TEXT, "", ATTRIBUTE_DUPLICATION, "abc.a", "pa");
    }

    @Test
    public void invalidEntity() {
        expectExceptionOnCreateInvalidAttribute("a", null, "abc123", TEXT, "", ENTITY_NOT_FOUND, "a");

        createEntity("abc", "a");

        expectExceptionOnCreateInvalidAttribute("abca", null, "abc123", TEXT, "", ENTITY_NOT_FOUND, "abca");
        expectExceptionOnCreateInvalidAttribute("a", null, "abc123", TEXT, "", ENTITY_NOT_FOUND, "a");
        expectExceptionOnCreateInvalidAttribute("abc.b", null, "abc123", TEXT, "", ENTITY_NOT_FOUND, "abc.b");

        createEntity("", "b");

        expectExceptionOnCreateInvalidAttribute("a", null, "abc123", null, "", ENTITY_NOT_FOUND, "a");
        expectExceptionOnCreateInvalidAttribute("abc.b", null, "abc123", null, "", ENTITY_NOT_FOUND, "abc.b");
    }

    @Test
    public void validNewName() {
        createEntity("abc", "a");
        PropertyType createdAttribute = createOneAttribute("abc.a", null, "pa", LONGTEXT, null);

        PropertyType updatedAttribute = updateAttribute("abc.a", createdAttribute, 1, "pb", LONGTEXT, null);
        verifyUpdatedAttribute(createdAttribute, updatedAttribute);
    }

    @Test
    public void invalidNewName() {
        createEntity("abc", "a");
        PropertyType createdAttribute = createOneAttribute("abc.a", null, "pa", LONGTEXT, null);
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "", "The name of an Attribute is mandatory");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, null, "The name of an Attribute is mandatory");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "p a", "Invalid value for Attribute name: p a");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a$", "Invalid value for Attribute name: a$");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a#", "Invalid value for Attribute name: a#");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a=", "Invalid value for Attribute name: a=");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a'", "Invalid value for Attribute name: a'");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a.", "Invalid value for Attribute name: a.");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a.a", "Invalid value for Attribute name: a.a");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a/a", "Invalid value for Attribute name: a/a");
        expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a*", "Invalid value for Attribute name: a*");
    }

    @Test
    public void validNewSequence() {
        createEntity("abc", "a");
        PropertyType createdAttribute1 = createOneAttribute("abc.a", null, "pa", TEXT, null);
        PropertyType createdAttribute2 = createOneAttribute("abc.a", null, "pb", TEXT, null);

        PropertyType updatedAttribute1 = updateAttribute("abc.a", createdAttribute1, 2, "pa", TEXT, null);
        verifyUpdatedAttribute(createdAttribute1, updatedAttribute1);

        PropertyType updatedAttribute2 = updateAttribute("abc.a", createdAttribute2, 2, "pb", TEXT, null);
        verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);

        PropertyType updatedAttribute3 = updateAttribute("abc.a", updatedAttribute2, 1, "pb", TEXT, null);
        verifyUpdatedAttribute(updatedAttribute2, updatedAttribute3);
    }

    @Test
    public void invalidNewSequence() {
        createEntity("abc", "a");

        PropertyType createdAttribute = createOneAttribute("abc.a", null, "pa", TEXT, null);

        createOneAttribute("abc.a", null, "pb", TEXT, null);

        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, null, "pa", LONGTEXT, null,
                "Invalid value for Attribute sequence: null");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, -1, "pa", LONGTEXT, null,
                "Invalid value for Attribute sequence: -1");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 0, "pa", LONGTEXT, null,
                "Invalid value for Attribute sequence: 0");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 3, "pa", LONGTEXT, null,
                "Invalid value for Attribute sequence: 3");
    }

    @Test
    public void changeType() {
        createEntity("abc", "a");
        PropertyType createdAttribute = createOneAttribute("abc.a", null, "pa", TEXT, null);

        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", null, null,
                "Can not change the type of an attribute");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT, null,
                "Can not change the type of an attribute");
    }

    @Test
    public void renamingConflicts() {
        createEntity("abc", "a");
        createEntity("abc", "b");

        createOneAttribute("abc.a", null, "pa", TEXT, null);

        PropertyType createdAttribute2 = createOneAttribute("abc.b", null, "pb", TEXT, null);
        PropertyType createdAttribute3 = createOneAttribute("abc.a", null, "pc", TEXT, null);

        PropertyType updatedAttribute2 = updateAttribute("abc.b", createdAttribute2, 1, "pa", TEXT, null);
        verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);

        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute3, 1, "pa", TEXT, null,
                "Attribute duplication on abc.a Entity. It already has an attribute pa.");

        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute3, 1, "pA", TEXT, null,
                "Attribute duplication on abc.a Entity. It already has an attribute pa.");

        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute3, 1, "PA", TEXT, null,
                "Attribute duplication on abc.a Entity. It already has an attribute pa.");
    }

    @Test
    public void genericChangeConfiguration() {
        createEntity("abc", "a");

        PropertyType createdAttribute1 = createOneAttribute("abc.a", null, "pa", TEXT, MANDATORY_TRUE);

        PropertyType createdAttribute2 = createOneAttribute("abc.a", null, "pb", TEXT, null);

        PropertyType updatedAttribute11 = updateAttribute("abc.a", createdAttribute1, 1, "pa", TEXT,
                "{\"mandatory\":false}");
        verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);

        PropertyType updatedAttribute12 = updateAttribute("abc.a", updatedAttribute11, 2, "pa", TEXT, null);
        verifyUpdatedAttribute(updatedAttribute11, updatedAttribute12);

        PropertyType updatedAttribute2 = updateAttribute("abc.a", createdAttribute2, 2, "pb", TEXT, MANDATORY_TRUE);
        verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);
    }

}