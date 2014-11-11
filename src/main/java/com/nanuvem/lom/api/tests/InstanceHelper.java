package com.nanuvem.lom.api.tests;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.api.Attribute;
import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.AttributeValue;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.Instance;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.util.JsonNodeUtil;

public class InstanceHelper {

	public static Instance createOneInstance(Facade facade,
			String entityFullName, AttributeValue... values) {

		Entity entity = null;
		if (entityFullName != null) {
			entity = facade.findEntityByFullName(entityFullName);
		}

		Instance instance = new Instance();
		instance.setEntity(entity);

		for (AttributeValue value : values) {
			value.setInstance(instance);
			instance.getValues().add(value);
		}
		return facade.create(instance);
	}

	public static void expectExceptionOnCreateInvalidInstance(Facade facade,
			String entityFullName, String exceptedMessage,
			AttributeValue... values) {

		try {
			createOneInstance(facade, entityFullName, values);
			fail();
		} catch (MetadataException metadataException) {
			Assert.assertEquals(exceptedMessage, metadataException.getMessage());
		}
	}

	public static void createAndVerifyOneInstance(Facade facade,
			String entityFullName, AttributeValue... values) {

		Instance createdInstance = createOneInstance(facade, entityFullName,
				values);

		Assert.assertNotNull(createdInstance.getId());
		Assert.assertEquals(new Integer(0), createdInstance.getVersion());
		Assert.assertEquals(createdInstance,
				facade.findInstanceById(createdInstance.getId()));
		Assert.assertEquals(entityFullName, createdInstance.getEntity()
				.getFullName());

		verifyAllAttributesValues(createdInstance, values);
	}

	private static void verifyAllAttributesValues(Instance createdInstance,
			AttributeValue... values) {

		boolean wereAllAttributeValuesValidated = true;

		for (AttributeValue attributeValue : values) {
			boolean valueParameterOfTheInteractionWasValidated = false;
			for (AttributeValue createdValue : createdInstance.getValues()) {
				Assert.assertNotNull("Id was null", createdValue.getId());
				try {
					if (existsDefaultConfiguration(attributeValue)) {

						valueParameterOfTheInteractionWasValidated = validateThatDefaultConfigurationWasAppliedToValue(createdValue);
						break;

					} else {
						valueParameterOfTheInteractionWasValidated = createdValue
								.equals(attributeValue);
						break;
					}

				} catch (Exception e) {
					fail();
				}
				wereAllAttributeValuesValidated = wereAllAttributeValuesValidated
						&& valueParameterOfTheInteractionWasValidated;
			}

		}
		Assert.assertTrue("There has been no validated AttributeValue",
				wereAllAttributeValuesValidated);
	}

	private static boolean existsDefaultConfiguration(
			AttributeValue attributeValue) {

		return (attributeValue.getValue() == null)
				&& (attributeValue.getAttribute().getConfiguration() != null)
				&& (attributeValue.getAttribute().getConfiguration()
						.contains(Attribute.DEFAULT_CONFIGURATION_NAME));
	}

	public static AttributeValue newAttributeValue(Facade facade,
			String attributeName, String entityFullName, String value) {

		AttributeValue attributeValue = new AttributeValue();
		attributeValue.setAttribute(facade
				.findAttributeByNameAndEntityFullName(attributeName,
						entityFullName));
		attributeValue.setValue(value);
		return attributeValue;
	}

	private static boolean validateThatDefaultConfigurationWasAppliedToValue(
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
		return attributeValue.getValue().equals(defaultField);
	}

	static AttributeValue attributeValue(String attributeName, String objValue) {
		Attribute attribute = new Attribute();
		attribute.setName(attributeName);
		AttributeValue value = new AttributeValue();
		value.setValue(objValue);
		value.setAttribute(attribute);
		return value;
	}

	public static void invalidValueForInstance(Facade facade,
			String entityName, Integer sequence, String attributeName,
			AttributeType type, String configuration, String value,
			String expectedMessage) {

		AttributeHelper.createOneAttribute(facade, entityName, sequence,
				attributeName, type, configuration);

		AttributeValue attributeValue = InstanceHelper.newAttributeValue(
				facade, attributeName, entityName, value);

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade,
				entityName, expectedMessage, attributeValue);

	}
}