package com.nanuvem.lom.api.tests.attribute;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import com.nanuvem.lom.api.Attribute;
import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.MetadataException;

public class AttributeHelper {

    private static Facade facade;

    public static final AttributeType TEXT = AttributeType.TEXT;
    public static final AttributeType LONGTEXT = AttributeType.LONGTEXT;
    public static final AttributeType PASSWORD = AttributeType.PASSWORD;

    static final String MANDATORY_FALSE = "{\"mandatory\":false}";
    static final String MANDATORY_TRUE = "{\"mandatory\":true}";

    static final String INVALID_SEQUENCE = "Invalid value for Attribute sequence: %1$s";
    static final String ATTRIBUTE_IS_MANDATORY = "The %1$s of an Attribute is mandatory";
    static final String ATTRIBUTE_DUPLICATION = "Attribute duplication on %1$s Entity. It already has an attribute %2$s.";
    static final String ENTITY_NOT_FOUND = "Entity not found: %1$s";

    public static void setFacade(Facade facade) {
        AttributeHelper.facade = facade;
    }

    public static Attribute createOneAttribute(String entityFullName, Integer attributeSequence, String attributeName,
            AttributeType attributeType, String attributeConfiguration) {

        Entity entity = facade.findEntityByFullName(entityFullName);
        Attribute attribute = new Attribute();
        attribute.setName(attributeName);

        attribute.setEntity(entity);
        attribute.setSequence(attributeSequence);
        attribute.setType(attributeType);
        attribute.setConfiguration(attributeConfiguration);
        attribute = facade.create(attribute);

        return attribute;
    }

    public static void expectExceptionOnCreateInvalidAttribute(String entityFullName, Integer attributeSequence,
            String attributeName, AttributeType attributeType, String attributeConfiguration, String expectedMessage,
            String... args) {

        try {
            createOneAttribute(entityFullName, attributeSequence, attributeName, attributeType, attributeConfiguration);
            fail();
        } catch (MetadataException e) {
            String formatedMessage = String.format(expectedMessage, (Object[]) args);
            Assert.assertEquals(formatedMessage, e.getMessage());
        }
    }

    public static void createAndVerifyOneAttribute(String entityFullName, Integer attributeSequence,
            String attributeName, AttributeType attributeType, String attributeConfiguration) {

        Attribute createdAttribute = createOneAttribute(entityFullName, attributeSequence, attributeName,
                attributeType, attributeConfiguration);

        Assert.assertNotNull(createdAttribute.getId());
        Assert.assertEquals(new Integer(0), createdAttribute.getVersion());
        Assert.assertEquals(createdAttribute, facade.findAttributeById(createdAttribute.getId()));
    }

    public static Attribute updateAttribute(String fullEntityName, Attribute oldAttribute, Integer newSequence,
            String newName, AttributeType newType, String newConfiguration) {

        Attribute attribute = facade.findAttributeByNameAndEntityFullName(oldAttribute.getName(), fullEntityName);

        attribute.setSequence(newSequence);
        attribute.setName(newName);
        attribute.setType(newType);
        attribute.setConfiguration(newConfiguration);
        attribute.setId(oldAttribute.getId());
        attribute.setVersion(oldAttribute.getVersion());

        return facade.update(attribute);
    }

    public static void expectExceptionOnUpdateInvalidAttribute(String entityFullName, Attribute oldAttribute,
            Integer newSequence, String newName, AttributeType newType, String newConfiguration, String exceptedMessage) {

        try {
            updateAttribute(entityFullName, oldAttribute, newSequence, newName, newType, newConfiguration);
            fail();
        } catch (MetadataException metadataException) {
            Assert.assertEquals(exceptedMessage, metadataException.getMessage());
        }
    }

    public static void expectExceptionOnUpdateWithInvalidNewName(Attribute createdAttribute, String invalidNewName,
            String exceptedMessage) {
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, invalidNewName, LONGTEXT, null,
                exceptedMessage);
    }

    public static void verifyUpdatedAttribute(Attribute attributeBeforeUpgrade, Attribute updatedAttribute) {

        Assert.assertNotNull("updatedAttribute.id should not be null", updatedAttribute.getId());

        int versionIncrementedInCreateAttribute = attributeBeforeUpgrade.getVersion() + 1;
        Assert.assertEquals("updatedAttribute.version should be " + versionIncrementedInCreateAttribute,
                versionIncrementedInCreateAttribute, (int) updatedAttribute.getVersion());

        Attribute listedAttribute = facade.findAttributeById(attributeBeforeUpgrade.getId());
        Assert.assertEquals("listedAttribute should be to updatedAttribute", updatedAttribute, listedAttribute);

        Assert.assertFalse("listedAttribute should be to createdAttribute",
                attributeBeforeUpgrade.equals(listedAttribute));
    }

}
