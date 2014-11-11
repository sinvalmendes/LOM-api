package com.nanuvem.lom.api.tests;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import com.nanuvem.lom.api.Attribute;
import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.MetadataException;

public class AttributeHelper {

	public static final AttributeType TEXT = AttributeType.TEXT;
	public static final AttributeType LONGTEXT = AttributeType.LONGTEXT;
	public static final AttributeType PASSWORD = AttributeType.PASSWORD;

	public static Attribute createOneAttribute(
			Facade facade, String entityFullName,
			Integer attributeSequence, String attributeName,
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

	public static void expectExceptionOnCreateInvalidAttribute(
			Facade facade, String entityFullName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration,
			String exceptedMessage) {

		try {
			createOneAttribute(facade, entityFullName,
					attributeSequence, attributeName, attributeType,
					attributeConfiguration);
			fail();
		} catch (MetadataException metadataException) {
			Assert.assertEquals(exceptedMessage, metadataException.getMessage());
		}
	}

	public static void createAndVerifyOneAttribute(
			Facade facade, String entityFullName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration) {

		Attribute createdAttribute = createOneAttribute(facade,
				entityFullName, attributeSequence, attributeName, attributeType,
				attributeConfiguration);

		Assert.assertNotNull(createdAttribute.getId());
		Assert.assertEquals(new Integer(0), createdAttribute.getVersion());
		Assert.assertEquals(createdAttribute,
				facade.findAttributeById(createdAttribute.getId()));
	}

	public static Attribute updateAttribute(
			Facade facade, String fullEntityName,
			Attribute oldAttribute, Integer newSequence, String newName,
			AttributeType newType, String newConfiguration) {

		Attribute attribute = facade
				.findAttributeByNameAndEntityFullName(oldAttribute.getName(),
						fullEntityName);

		attribute.setSequence(newSequence);
		attribute.setName(newName);
		attribute.setType(newType);
		attribute.setConfiguration(newConfiguration);
		attribute.setId(oldAttribute.getId());
		attribute.setVersion(oldAttribute.getVersion());

		return facade.update(attribute);
	}

	public static void expectExceptionOnUpdateInvalidAttribute(
			Facade facade, String entityFullName,
			Attribute oldAttribute, Integer newSequence, String newName,
			AttributeType newType, String newConfiguration,
			String exceptedMessage) {

		try {
			updateAttribute(facade, entityFullName, oldAttribute,
					newSequence, newName, newType, newConfiguration);
			fail();
		} catch (MetadataException metadataException) {
			Assert.assertEquals(exceptedMessage, metadataException.getMessage());
		}
	}

}
