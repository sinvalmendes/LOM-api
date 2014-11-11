package com.nanuvem.lom.api.tests;

import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.AttributeValue;
import com.nanuvem.lom.api.Facade;

public abstract class InstanceServiceTest {

	private Facade facade;

	public abstract Facade createFacade();

	@Before
	public void init() {
		facade = createFacade();
	}

	@Test
	public void unknownClass() {
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "a",
				"Entity not found: a", InstanceHelper.attributeValue("age", "30"));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				"Entity not found: abc.a",
				InstanceHelper.attributeValue("age", "30"));
	}

	@Test
	public void nullClass() {
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, null,
				"Invalid value for Instance class: The class is mandatory",
				InstanceHelper.attributeValue("age", "30"));
	}

	@Test
	public void classWithoutAttributes() {
		EntityHelper.createEntity(facade, "abc", "a");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a");

		EntityHelper.createEntity(facade, "abc", "b");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b");

		EntityHelper.createEntity(facade, "", "a");
		InstanceHelper.createAndVerifyOneInstance(facade, "a");

		EntityHelper.createEntity(facade, "", "b");
		InstanceHelper.createAndVerifyOneInstance(facade, "b");
	}

	@Test
	public void classWithoutAttributesUnknownAttributes() {
		EntityHelper.createEntity(facade, "system", "Client");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade,
				"system.Client", "Unknown attribute for system.Client: age",
				InstanceHelper.attributeValue("age", "30"));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade,
				"system.Client", "Unknown attribute for system.Client: height",
				InstanceHelper.attributeValue("height", "1.85"));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade,
				"system.Client", "Unknown attribute for system.Client: name",
				InstanceHelper.attributeValue("name", "John"));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade,
				"system.Client", "Unknown attribute for system.Client: active",
				InstanceHelper.attributeValue("active", "true"));
	}

	@Test
	public void classWithKnownAttributesAndWithoutConfiguration() {
		EntityHelper.createEntity(facade, "system", "Client");
		AttributeHelper.createOneAttribute(facade, "system.Client", null, "pa",
				AttributeType.TEXT, null);

		EntityHelper.createEntity(facade, "system", "User");
		AttributeHelper.createOneAttribute(facade, "system.User", null, "pa",
				AttributeType.TEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.User", null, "pb",
				AttributeType.LONGTEXT, null);

		EntityHelper.createEntity(facade, "system", "Organization");
		AttributeHelper.createOneAttribute(facade, "system.Organization", null,
				"pa", AttributeType.TEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.Organization", null,
				"pb", AttributeType.LONGTEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.Organization", null,
				"pc", AttributeType.INTEGER, null);

		EntityHelper.createEntity(facade, "system", "Category");
		AttributeHelper.createOneAttribute(facade, "system.Category", null,
				"pa", AttributeType.TEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.Category", null,
				"pb", AttributeType.LONGTEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.Category", null,
				"pc", AttributeType.INTEGER, null);
		AttributeHelper.createOneAttribute(facade, "system.Category", null,
				"pd", AttributeType.PASSWORD, null);

		InstanceHelper.createAndVerifyOneInstance(facade, "system.Client");
		InstanceHelper.createAndVerifyOneInstance(facade, "system.User");
		InstanceHelper
				.createAndVerifyOneInstance(facade, "system.Organization");
		InstanceHelper.createAndVerifyOneInstance(facade, "system.Category");
	}

	@Test
	public void instanceWithValidValuesForTheConfigurationOfAttributesText() {
		EntityHelper.createEntity(facade, "abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "name",
				AttributeType.TEXT, "{\"mandatory\": true}");
		AttributeValue value1 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.a", "Jose");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", value1);

		EntityHelper.createEntity(facade, "abc", "b");
		AttributeHelper.createOneAttribute(facade, "abc.b", null, "name",
				AttributeType.TEXT,
				"{\"mandatory\": true, \"default\": \"Michael\"}");
		AttributeValue value2 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.b", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b", value2);

		EntityHelper.createEntity(facade, "abc", "c");
		AttributeHelper
				.createOneAttribute(facade, "abc.c", null, "name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 6}");
		AttributeValue value3 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.c", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.c", value3);

		EntityHelper.createEntity(facade, "abc", "d");
		AttributeHelper.createOneAttribute(facade, "abc.d", null, "name",
				AttributeType.TEXT, "{\"mandatory\": true, \"minlength\": 6}");
		AttributeValue value4 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.d", "Johson");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.d", value4);

		EntityHelper.createEntity(facade, "abc", "e");
		AttributeHelper
				.createOneAttribute(facade, "abc.e", null, "name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"maxlength\" : 6}");
		AttributeValue value5 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.e", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.e", value5);

		EntityHelper.createEntity(facade, "abc", "f");
		AttributeHelper.createOneAttribute(facade, "abc.f", null, "name",
				AttributeType.TEXT, "{\"mandatory\": true, \"maxlength\" : 6}");
		AttributeValue value6 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.f", "Johson");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.f", value6);

		EntityHelper.createEntity(facade, "abc", "g");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.g",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 6, \"maxlength\" : 6}");
		AttributeValue value7 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.g", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.g", value7);

		EntityHelper.createEntity(facade, "abc", "i");
		AttributeHelper.createOneAttribute(facade, "abc.i", null, "name",
				AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 6, \"maxlength\" : 6}");
		AttributeValue value8 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.i", "Johson");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.i", value8);

		EntityHelper.createEntity(facade, "abc", "j");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.j",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 3, \"maxlength\" : 8}");
		AttributeValue value9 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.j", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.j", value9);

		EntityHelper.createEntity(facade, "abc", "k");
		AttributeHelper.createOneAttribute(facade, "abc.k", null, "name",
				AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 3, \"maxlength\" : 8}");
		AttributeValue value10 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.k", "Johnson");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.k", value10);

		EntityHelper.createEntity(facade, "abc", "l");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.l",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"abc@abc.com\", \"minlength\": 3, \"maxlength\" : 15, \"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\"}");
		AttributeValue value11 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.l", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.l", value11);

		EntityHelper.createEntity(facade, "abc", "m");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.m",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"minlength\": 3, \"maxlength\" : 15, \"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\"}");
		AttributeValue value12 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.m", "abc@abc.com");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.m", value12);
	}

	@Test
	public void instanceWithValidValuesForTheConfigurationOfAttributesLongText() {
		EntityHelper.createEntity(facade, "abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "name",
				AttributeType.LONGTEXT, "{\"mandatory\": true}");
		AttributeValue value1 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.a", "Jose");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", value1);

		EntityHelper.createEntity(facade, "abc", "b");
		AttributeHelper.createOneAttribute(facade, "abc.b", null, "name",
				AttributeType.LONGTEXT, "{\"default\" : \"default longtext\"}");
		AttributeValue value2 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.b", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b", value2);

		EntityHelper.createEntity(facade, "abc", "c");
		AttributeHelper.createOneAttribute(facade, "abc.c", null, "name",
				AttributeType.LONGTEXT, "{\"default\" : \"default longtext\"}");
		AttributeValue value3 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.c", "Jose");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.c", value3);

		EntityHelper.createEntity(facade, "abc", "d");
		AttributeHelper.createOneAttribute(facade, "abc.d", null, "name",
				AttributeType.LONGTEXT, "{\"minlength\" : 6}");
		AttributeValue value4 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.d", "Johson");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.d", value4);

		EntityHelper.createEntity(facade, "abc", "e");
		AttributeHelper.createOneAttribute(facade, "abc.e", null, "name",
				AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"minlength\" : 6}");
		AttributeValue value5 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.e", "Johson");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.e", value5);

		EntityHelper.createEntity(facade, "abc", "f");
		AttributeHelper
				.createOneAttribute(facade, "abc.f", null, "name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
		AttributeValue value6 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.f", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.f", value6);

		EntityHelper.createEntity(facade, "abc", "g");
		AttributeHelper
				.createOneAttribute(facade, "abc.g", null, "name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
		AttributeValue value7 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.g", "abccab");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.g", value7);

		EntityHelper.createEntity(facade, "abc", "h");
		AttributeHelper.createOneAttribute(facade, "abc.h", null, "name",
				AttributeType.LONGTEXT, "{\"maxlength\" : 6}");
		AttributeValue value8 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.h", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.h", value8);

		EntityHelper.createEntity(facade, "abc", "i");
		AttributeHelper.createOneAttribute(facade, "abc.i", null, "name",
				AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"maxlength\" : 6}");
		AttributeValue value9 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.i", "Johson");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.i", value9);

		EntityHelper.createEntity(facade, "abc", "j");
		AttributeHelper
				.createOneAttribute(facade, "abc.j", null, "name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"maxlength\" : 6}");
		AttributeValue value10 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.j", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.j", value10);

		EntityHelper.createEntity(facade, "abc", "k");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.k",
						null,
						"name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"minlength\" : 6, \"maxlength\" : 6}");
		AttributeValue value11 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.k", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.k", value11);

		EntityHelper.createEntity(facade, "abc", "l");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.l",
						null,
						"name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"minlength\" : 6, \"maxlength\" : 6}");
		AttributeValue value12 = InstanceHelper.newAttributeValue(facade,
				"name", "abc.l", "abccba");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.l", value12);
	}

	public void instanceWithValidValuesForTheConfigurationOfAttributesInteger() {
		EntityHelper.createEntity(facade, "abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "number",
				AttributeType.INTEGER, "{\"default\": 1}");
		AttributeValue value1 = InstanceHelper.newAttributeValue(facade,
				"number", "abc.a", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", value1);

		EntityHelper.createEntity(facade, "abc", "b");
		AttributeHelper.createOneAttribute(facade, "abc.b", null, "number",
				AttributeType.INTEGER, "{\"minvalue\": 1}");
		AttributeValue value2 = InstanceHelper.newAttributeValue(facade,
				"number", "abc.b", "1");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b", value2);

		EntityHelper.createEntity(facade, "abc", "c");
		AttributeHelper.createOneAttribute(facade, "abc.c", null, "number",
				AttributeType.INTEGER, "{\"maxvalue\": 1}");
		AttributeValue value3 = InstanceHelper.newAttributeValue(facade,
				"number", "abc.c", "1");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.c", value3);

		EntityHelper.createEntity(facade, "abc", "d");
		AttributeHelper.createOneAttribute(facade, "abc.d", null, "number",
				AttributeType.INTEGER, "{\"minvalue\": 1, \"maxvalue\": 1}");
		AttributeValue value4 = InstanceHelper.newAttributeValue(facade,
				"number", "abc.d", "1");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.d", value4);

		EntityHelper.createEntity(facade, "abc", "e");
		AttributeHelper.createOneAttribute(facade, "abc.e", null, "number",
				AttributeType.INTEGER,
				"{\"default\": 1, \"minvalue\": 1, \"maxvalue\": 1}");
		AttributeValue value5 = InstanceHelper.newAttributeValue(facade,
				"number", "abc.e", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.e", value5);

		EntityHelper.createEntity(facade, "abc", "f");
		AttributeHelper
				.createOneAttribute(facade, "abc.f", null, "number",
						AttributeType.INTEGER,
						"{\"mandatory\": true,\"default\": 1, \"minvalue\": 1, \"maxvalue\": 1}");
		AttributeValue value6 = InstanceHelper.newAttributeValue(facade,
				"number", "abc.f", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.f", value6);
	}

	public void instanceWithValidValuesForTheConfigurationOfAttributesPassword() {
		EntityHelper.createEntity(facade, "abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "secretKey",
				AttributeType.PASSWORD, "{\"default\": \"password\"}");
		AttributeValue value1 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.a", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", value1);

		EntityHelper.createEntity(facade, "abc", "b");
		AttributeHelper.createOneAttribute(facade, "abc.b", null, "secretKey",
				AttributeType.PASSWORD, "{\"minUppers\": 2}");
		AttributeValue value2 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.b", "SecretKey");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b", value2);

		EntityHelper.createEntity(facade, "abc", "c");
		AttributeHelper.createOneAttribute(facade, "abc.c", null, "secretKey",
				AttributeType.PASSWORD, "{\"minNumbers\": 2}");
		AttributeValue value3 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.c", "1secretkey2");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.c", value3);

		EntityHelper.createEntity(facade, "abc", "d");
		AttributeHelper.createOneAttribute(facade, "abc.d", null, "secretKey",
				AttributeType.PASSWORD, "{\"minSymbols\": 2}");
		AttributeValue value4 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.d", "&secretkey%");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.d", value4);

		EntityHelper.createEntity(facade, "abc", "e");
		AttributeHelper.createOneAttribute(facade, "abc.e", null, "secretKey",
				AttributeType.PASSWORD, "{\"maxRepeat\": 2}");
		AttributeValue value5 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.e", "seecretkey");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.e", value5);

		EntityHelper.createEntity(facade, "abc", "f");
		AttributeHelper.createOneAttribute(facade, "abc.f", null, "secretKey",
				AttributeType.PASSWORD, "{\"minlength\": 6}");
		AttributeValue value6 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.f", "secret");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.f", value6);

		EntityHelper.createEntity(facade, "abc", "g");
		AttributeHelper.createOneAttribute(facade, "abc.g", null, "secretKey",
				AttributeType.PASSWORD, "{\"maxlength\": 6}");
		AttributeValue value7 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.g", "secret");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.g", value7);

		EntityHelper.createEntity(facade, "abc", "h");
		AttributeHelper.createOneAttribute(facade, "abc.h", null, "secretKey",
				AttributeType.PASSWORD, "{\"mandatory\": true}");
		AttributeValue value8 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.h", "secret");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.h", value8);

		EntityHelper.createEntity(facade, "abc", "i");
		AttributeHelper.createOneAttribute(facade, "abc.i", null, "secretKey",
				AttributeType.PASSWORD, "{\"mandatory\": false}");
		AttributeValue value9 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.i", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.h", value9);

		EntityHelper.createEntity(facade, "abc", "j");
		AttributeHelper.createOneAttribute(facade, "abc.j", null, "secretKey",
				AttributeType.PASSWORD,
				"{\"mandatory\": true, \"default\": \"mypassword\"}");
		AttributeValue value10 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.j", null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.j", value10);

		EntityHelper.createEntity(facade, "abc", "k");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.k",
						null,
						"secretKey",
						AttributeType.PASSWORD,
						"{\"mandatory\": true, \"default\": \"P@ssW04\", \"minlength\": 6, "
								+ "\"maxlength\": 9, \"minUppers\": 2, \"minNumbers\": 2, "
								+ "\"minSymbols\": 2, \"maxRepeat\": 2}");
		AttributeValue value11 = InstanceHelper.newAttributeValue(facade,
				"secretKey", "abc.k", "0tH3r@ss");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.k", value11);
	}

	@Test
	public void instanceWithInvalidValuesForIntegerTypeAttributeWithoutConfiguration() {
		String messageException = "Invalid value for the Instance. The value for the 'name' attribute must be an int";

		EntityHelper.createEntity(facade, "abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "name",
				AttributeType.INTEGER, "");

		AttributeValue value = InstanceHelper.newAttributeValue(facade, "name",
				"abc.a", "false");
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, value);

		value = InstanceHelper.newAttributeValue(facade, "name", "abc.a", "true");
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, value);

		value = InstanceHelper.newAttributeValue(facade, "name", "abc.a", "pa");
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, value);

		value = InstanceHelper.newAttributeValue(facade, "name", "abc.a", "3.2");
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, value);

		value = InstanceHelper.newAttributeValue(facade, "name", "abc.a", "0.75");
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, value);

		value = InstanceHelper.newAttributeValue(facade, "name", "abc.a", "-3.2");
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, value);
	}

	@Test
	public void instanceWithInvalidValuesForConfigurationOfTextAttributes() {
		EntityHelper.createEntity(facade, "abc", "a");

		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameA",
						AttributeType.TEXT,
						"{\"mandatory\" : true}",
						null,
						"Invalid value for the Instance. The value for the 'nameA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameB",
						AttributeType.TEXT,
						"{\"minlength\" : 5}",
						"abcd",
						"Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameBA",
						AttributeType.TEXT,
						"{\"mandatory\" : true, \"minlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, The value for 'nameBA' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameC",
						AttributeType.TEXT,
						"{\"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameCA",
						AttributeType.TEXT,
						"{\"mandatory\" : true, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameD",
						AttributeType.TEXT,
						"{\"minlength\" : 5, \"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameD' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameDA",
						AttributeType.TEXT,
						"{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameDA' attribute is mandatory, The value for 'nameDA' must have a minimum length of 5 characters");
//		InstanceHelper
//				.invalidValueForInstance(
//						facade,
//						"abc.a",
//						null,
//						"nameE",
//						AttributeType.TEXT,
//						"{\"regex\" :\"\\d\\d\\d([,\\s])?\\d\\d\\d\\d\"}",
//						"12345678",
//						"Invalid value for the Instance. The value of the 'nameE' attribute does not meet the defined regular expression");
//		InstanceHelper
//				.invalidValueForInstance(
//						facade,
//						"abc.a",
//						null,
//						"nameF",
//						AttributeType.TEXT,
//						"{\"mandatory\" : true, \"minlength\" : 4, \"maxlength\" : 4, \"regex\" : \"\\d\\d\\d\\s\\d\\d\\d\\s\\d\\d\\d\\s\\d\\d\"}",
//						"123 456 789",
//						"Invalid value for the Instance. The value for 'nameF' must have a maximum length 4 characters, The value of the 'nameF' attribute does not meet the defined regular expression");
	}

	@Test
	public void instanceWithInvalidValuesForTheConfigurationOfAttributesLongText() {
		EntityHelper.createEntity(facade, "abc", "a");

		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameA",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true}",
						null,
						"Invalid value for the Instance. The value for the 'nameA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameB",
						AttributeType.LONGTEXT,
						"{\"minlength\" : 5}",
						"abcd",
						"Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameBA",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"minlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, The value for 'nameBA' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameC",
						AttributeType.LONGTEXT,
						"{\"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameCA",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameD",
						AttributeType.LONGTEXT,
						"{\"minlength\" : 5, \"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameD' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameE",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameE' attribute is mandatory, The value for 'nameE' must have a minimum length of 5 characters");
	}

	@Test
	public void instanceWithInvalidValuesForTheConfigurationOfAttributesInteger() {
		EntityHelper.createEntity(facade, "abc", "a");

		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameA",
						AttributeType.INTEGER,
						"{\"mandatory\" : true}",
						null,
						"Invalid value for the Instance. The value for the 'nameA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameB",
						AttributeType.INTEGER,
						"{\"minvalue\" : 3}",
						"2",
						"Invalid value for the Instance. The value for 'nameB' must be greater or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameC",
						AttributeType.INTEGER,
						"{\"mandatory\" : true, \"minvalue\" : 3}",
						null,
						"Invalid value for the Instance. The value for the 'nameC' attribute is mandatory, The value for 'nameC' must be greater or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameD",
						AttributeType.INTEGER,
						"{\"maxvalue\" : 3}",
						"4",
						"Invalid value for the Instance. The value for 'nameD' must be smaller or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameE",
						AttributeType.INTEGER,
						"{\"mandatory\" : true, \"maxvalue\" : 3}",
						null,
						"Invalid value for the Instance. The value for the 'nameE' attribute is mandatory, The value for 'nameE' must be smaller or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameF",
						AttributeType.INTEGER,
						"{\"minvalue\" : 3, \"maxvalue\" : 3}",
						"4",
						"Invalid value for the Instance. The value for 'nameF' must be smaller or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameG",
						AttributeType.INTEGER,
						"{\"mandatory\" : true, \"minvalue\" : 3, \"maxvalue\" : 3}",
						null,
						"Invalid value for the Instance. The value for the 'nameG' attribute is mandatory, The value for 'nameG' must be greater or equal to 3, The value for 'nameG' must be smaller or equal to 3");
	}

	@Test
	public void instanceWithInvalidValuesForTheConfigurationOfAttributesPassword() {
		EntityHelper.createEntity(facade, "abc", "a");

		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameA",
						AttributeType.PASSWORD,
						"{\"mandatory\" : true}",
						null,
						"Invalid value for the Instance. The value for the 'nameA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameB",
						AttributeType.PASSWORD,
						"{\"minlength\" : 5}",
						"abcd",
						"Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameBA",
						AttributeType.PASSWORD,
						"{\"mandatory\" : true, \"minlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, The value for 'nameBA' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameC",
						AttributeType.PASSWORD,
						"{\"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameCA",
						AttributeType.PASSWORD,
						"{\"mandatory\" : true, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameD",
						AttributeType.PASSWORD,
						"{\"minlength\" : 5, \"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameD' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameDA",
						AttributeType.PASSWORD,
						"{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameDA' attribute is mandatory, The value for 'nameDA' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameE",
						AttributeType.PASSWORD,
						"{\"minUppers\" : 3}",
						"ABcdef",
						"Invalid value for the Instance. The value for 'nameE' must have at least 3 uppercase characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameF",
						AttributeType.PASSWORD,
						"{\"minNumbers\" : 3}",
						"abc12def",
						"Invalid value for the Instance. The value for 'nameF' must be at least 3 numbers");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameG",
						AttributeType.PASSWORD,
						"{\"minSymbols\" : 3}",
						"ab%c12def*",
						"Invalid value for the Instance. The value for 'nameG' must have at least 3 symbol characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameH",
						AttributeType.PASSWORD,
						"{\"maxRepeat\" : 0}",
						"ab%c1a2e*",
						"Invalid value for the Instance. The value for 'nameH' must not have repeated characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameI",
						AttributeType.PASSWORD,
						"{\"maxRepeat\" : 1}",
						"ab%ac1a2e*",
						"Invalid value for the Instance. The value for 'nameI' must not have more than 2 repeated characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.a",
						null,
						"nameJ",
						AttributeType.PASSWORD,
						"{\"mandatory\": true, \"minlength\": 4, \"maxlength\": 4, \"minUppers\" : 3, \"minNumbers\" : 2, \"minSymbols\" : 1, \"maxRepeat\" : 0}",
						"ab1CfdeFa",
						"Invalid value for the Instance. The value for 'nameJ' must have a maximum length of 4 characters, The value for 'nameJ' must have at least 3 uppercase characters, The value for 'nameJ' must be at least 2 numbers, The value for 'nameJ' must have at least 1 symbol character, The value for 'nameJ' must not have repeated characters");

	}
}