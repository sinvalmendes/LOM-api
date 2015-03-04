package com.nanuvem.lom.api.tests.instance;

import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.api.PropertyType;
import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.Property;
import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.tests.attribute.AttributeHelper;
import com.nanuvem.lom.api.util.JsonNodeUtil;

public class InstanceHelper {

    private static Facade facade;

    public static void setFacade(Facade facade) {
        InstanceHelper.facade = facade;
    }

    public static Entity createOneInstance(EntityType entityType, String... values) {

        Entity entity = new Entity();
        entity.setEntity(entityType);

        for (int i = 0; i < values.length; i++) {
            Property property = new Property();
            property.setValue(values[i]);

            if (entityType != null) {
                property.setAttribute(entityType.getAttributes().get(i));
            }

            property.setInstance(entity);
            entity.getValues().add(property);
        }
        return facade.create(entity);
    }

    public static void expectExceptionOnCreateInvalidInstance(String entityFullName, String exceptedMessage,
            String... values) {

        try {
            EntityType entityType = null;
            if (entityFullName != null) {
                entityType = facade.findEntityByFullName(entityFullName);
            }

            createOneInstance(entityType, values);
            fail();
        } catch (MetadataException metadataException) {
            Assert.assertEquals(exceptedMessage, metadataException.getMessage());
        }
    }

    public static void createAndVerifyOneInstance(String entityFullName, String... values) {

        EntityType entityType = null;
        if (entityFullName != null) {
            entityType = facade.findEntityByFullName(entityFullName);
        }

        int numberOfInstances = facade.findInstancesByEntityId(entityType.getId()).size();

        Entity newInstance = createOneInstance(entityType, values);

        Assert.assertNotNull(newInstance.getId());
        Assert.assertEquals(new Integer(0), newInstance.getVersion());

        Entity createdInstance = facade.findInstanceById(newInstance.getId());
        Assert.assertEquals(newInstance, createdInstance);
        Assert.assertEquals(entityFullName, createdInstance.getEntity().getFullName());

        verifyAllAttributesValues(createdInstance, values);

        List<Entity> entities = facade.findInstancesByEntityId(entityType.getId());

        Assert.assertEquals(numberOfInstances + 1, entities.size());
        Entity listedInstance = entities.get(numberOfInstances);
        Assert.assertEquals(newInstance, listedInstance);
        Assert.assertEquals(entityFullName, listedInstance.getEntity().getFullName());

        verifyAllAttributesValues(listedInstance, values);

    }

    private static void verifyAllAttributesValues(Entity createdInstance, String... values) {

        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            Property createdValue = createdInstance.getValues().get(i);

            Assert.assertNotNull("Id was null", createdValue.getId());

            if (usesDefaultConfiguration(value, createdValue)) {
                validateThatDefaultConfigurationWasAppliedToValue(createdValue);
            } else {
                Assert.assertEquals(value, createdValue.getValue());
            }
        }
    }

    private static boolean usesDefaultConfiguration(String value, Property createdValue) {

        return ((value == null) || value.isEmpty()) && (createdValue.getAttribute().getConfiguration() != null)
                && (createdValue.getAttribute().getConfiguration().contains(PropertyType.DEFAULT_CONFIGURATION_NAME));
    }

    public static Property newAttributeValue(String attributeName, String entityFullName, String value) {

        Property property = new Property();
        property.setAttribute(facade.findAttributeByNameAndEntityFullName(attributeName, entityFullName));
        property.setValue(value);
        return property;
    }

    private static void validateThatDefaultConfigurationWasAppliedToValue(Property property) {
        JsonNode jsonNode = null;
        try {
            jsonNode = JsonNodeUtil.validate(property.getAttribute().getConfiguration(), null);
        } catch (Exception e) {
            fail();
            throw new RuntimeException("Json configuration is in invalid format");
        }
        String defaultField = jsonNode.get(PropertyType.DEFAULT_CONFIGURATION_NAME).asText();
        Assert.assertEquals(property.getValue(), defaultField);
    }

    static Property property(String attributeName, String objValue) {
        PropertyType propertyType = new PropertyType();
        propertyType.setName(attributeName);
        Property value = new Property();
        value.setValue(objValue);
        value.setAttribute(propertyType);
        return value;
    }

    public static void invalidValueForInstance(String entityName, Integer sequence, String attributeName,
            AttributeType type, String configuration, String value, String expectedMessage) {

        AttributeHelper.createOneAttribute(entityName, sequence, attributeName, type, configuration);

        InstanceHelper.expectExceptionOnCreateInvalidInstance(entityName, expectedMessage, value);

    }
}