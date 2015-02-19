package com.nanuvem.lom.api.tests.instance;

import static org.junit.Assert.fail;

import java.util.List;

import junit.framework.Assert;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.api.Attribute;
import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.AttributeValue;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.Instance;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.tests.attribute.AttributeHelper;
import com.nanuvem.lom.api.util.JsonNodeUtil;

public class InstanceHelper {

	private static Facade facade;

	public static void setFacade(Facade facade) {
		InstanceHelper.facade = facade;
	}

	public static Instance createOneInstance(Entity entity, String... values) {

		Instance instance = new Instance();
		instance.setEntity(entity);

		for (int i = 0; i < values.length; i++) {
			AttributeValue attributeValue = new AttributeValue();
			attributeValue.setValue(values[i]);

			if (entity != null) {
				attributeValue.setAttribute(entity.getAttributes().get(i));
			}

			attributeValue.setInstance(instance);
			instance.getValues().add(attributeValue);
		}
		return facade.create(instance);
	}

	public static void expectExceptionOnCreateInvalidInstance(
			String entityFullName, String exceptedMessage, String... values) {

		try {
			Entity entity = null;
			if (entityFullName != null) {
				entity = facade.findEntityByFullName(entityFullName);
			}

			createOneInstance(entity, values);
			fail();
		} catch (MetadataException metadataException) {
			Assert.assertEquals(exceptedMessage, metadataException.getMessage());
		}
	}

	public static void createAndVerifyOneInstance(String entityFullName,
			String... values) {

		Entity entity = null;
		if (entityFullName != null) {
			entity = facade.findEntityByFullName(entityFullName);
		}

		int numberOfInstances = facade.findInstancesByEntityId(entity.getId())
				.size();

		Instance newInstance = createOneInstance(entity, values);

		Assert.assertNotNull(newInstance.getId());
		Assert.assertEquals(new Integer(0), newInstance.getVersion());

		Instance createdInstance = facade.findInstanceById(newInstance.getId());
		Assert.assertEquals(newInstance, createdInstance);
		Assert.assertEquals(entityFullName, createdInstance.getEntity()
				.getFullName());

		verifyAllAttributesValues(createdInstance, values);

		List<Instance> instances = facade.findInstancesByEntityId(entity
				.getId());

		Assert.assertEquals(numberOfInstances + 1, instances.size());
		Instance listedInstance = instances.get(numberOfInstances);
		Assert.assertEquals(newInstance, listedInstance);
		Assert.assertEquals(entityFullName, listedInstance.getEntity()
				.getFullName());

		verifyAllAttributesValues(listedInstance, values);

	}

	private static void verifyAllAttributesValues(Instance createdInstance,
			String... values) {

		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			AttributeValue createdValue = createdInstance.getValues().get(i);

			Assert.assertNotNull("Id was null", createdValue.getId());

			if (usesDefaultConfiguration(value, createdValue)) {
				validateThatDefaultConfigurationWasAppliedToValue(createdValue);
			} else {
				Assert.assertEquals(value, createdValue.getValue());
			}
		}
	}

	private static boolean usesDefaultConfiguration(String value,
			AttributeValue createdValue) {

		return ((value == null) || value.isEmpty())
				&& (createdValue.getAttribute().getConfiguration() != null)
				&& (createdValue.getAttribute().getConfiguration()
						.contains(Attribute.DEFAULT_CONFIGURATION_NAME));
	}

	public static AttributeValue newAttributeValue(String attributeName,
			String entityFullName, String value) {

		AttributeValue attributeValue = new AttributeValue();
		attributeValue.setAttribute(facade
				.findAttributeByNameAndEntityFullName(attributeName,
						entityFullName));
		attributeValue.setValue(value);
		return attributeValue;
	}

	private static void validateThatDefaultConfigurationWasAppliedToValue(
			AttributeValue attributeValue) {
		JsonNode jsonNode = null;
		try {
			jsonNode = JsonNodeUtil.validate(attributeValue.getAttribute()
					.getConfiguration(), null);
		} catch (Exception e) {
			fail();
			throw new RuntimeException(
					"Json configuration is in invalid format");
		}
		String defaultField = jsonNode
				.get(Attribute.DEFAULT_CONFIGURATION_NAME).asText();
		Assert.assertEquals(attributeValue.getValue(), defaultField);
	}

	static AttributeValue attributeValue(String attributeName, String objValue) {
		Attribute attribute = new Attribute();
		attribute.setName(attributeName);
		AttributeValue value = new AttributeValue();
		value.setValue(objValue);
		value.setAttribute(attribute);
		return value;
	}

	public static void invalidValueForInstance(String entityName,
			Integer sequence, String attributeName, AttributeType type,
			String configuration, String value, String expectedMessage) {

		AttributeHelper.createOneAttribute(entityName, sequence, attributeName,
				type, configuration);

		InstanceHelper.expectExceptionOnCreateInvalidInstance(entityName,
				expectedMessage, value);

	}
}