package com.nanuvem.lom.api.tests;

import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.Facade;

public abstract class InstanceServiceTest {

	private Facade facade;

	public abstract Facade createFacade();

	@Before
	public void init() {
		facade = createFacade();
		EntityHelper.setFacade(facade);
	}

	@Test
	public void unknownClass() {
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "a",
				"Entity not found: a", "30");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				"Entity not found: abc.a", "30");
	}

	@Test
	public void nullClass() {
		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, null,
				"Invalid value for Instance entity: The entity is mandatory", "30");
	}

	@Test
	public void entityWithoutAttributes() {
		EntityHelper.createEntity("abc", "a");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a");

		EntityHelper.createEntity("abc", "b");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b");

		EntityHelper.createEntity("", "a");
		InstanceHelper.createAndVerifyOneInstance(facade, "a");

		EntityHelper.createEntity("", "b");
		InstanceHelper.createAndVerifyOneInstance(facade, "b");
	}

	@Test
	public void entityWithKnownAttributesAndWithoutConfiguration() {
		EntityHelper.createEntity("system", "Client");
		AttributeHelper.createOneAttribute(facade, "system.Client", null, "pa",
				AttributeType.TEXT, null);

		EntityHelper.createEntity("system", "User");
		AttributeHelper.createOneAttribute(facade, "system.User", null, "pa",
				AttributeType.TEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.User", null, "pb",
				AttributeType.LONGTEXT, null);

		EntityHelper.createEntity("system", "Organization");
		AttributeHelper.createOneAttribute(facade, "system.Organization", null,
				"pa", AttributeType.TEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.Organization", null,
				"pb", AttributeType.LONGTEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.Organization", null,
				"pc", AttributeType.INTEGER, null);

		EntityHelper.createEntity("system", "Category");
		AttributeHelper.createOneAttribute(facade, "system.Category", null,
				"pa", AttributeType.TEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.Category", null,
				"pb", AttributeType.LONGTEXT, null);
		AttributeHelper.createOneAttribute(facade, "system.Category", null,
				"pc", AttributeType.INTEGER, null);
		AttributeHelper.createOneAttribute(facade, "system.Category", null,
				"pd", AttributeType.PASSWORD, null);

		InstanceHelper.createAndVerifyOneInstance(facade, "system.client");
		InstanceHelper.createAndVerifyOneInstance(facade, "system.client", "va");
		InstanceHelper.createAndVerifyOneInstance(facade, "system.user");
		InstanceHelper.createAndVerifyOneInstance(facade, "system.user", "va", "vb");
		InstanceHelper
				.createAndVerifyOneInstance(facade, "system.organization");
		InstanceHelper
		.createAndVerifyOneInstance(facade, "system.organization", "va", "vb", "3");
		InstanceHelper.createAndVerifyOneInstance(facade, "system.category");
		InstanceHelper.createAndVerifyOneInstance(facade, "system.category", "va", "vb", "3", "vd");
	}

	@Test
	public void instanceWithValidValuesForConfigurationOfTextAttributes() {
		EntityHelper.createEntity("abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "name",
				AttributeType.TEXT, "{\"mandatory\": true}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", "Jose");

		EntityHelper.createEntity("abc", "a1");
		AttributeHelper.createOneAttribute(facade, "abc.a1", null, "name",
				AttributeType.TEXT, null);
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", "Jose");

		EntityHelper.createEntity("abc", "b");
		AttributeHelper.createOneAttribute(facade, "abc.b", null, "name",
				AttributeType.TEXT,
				"{\"mandatory\": true, \"default\": \"Michael\"}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b", (String) null);

		EntityHelper.createEntity("abc", "c");
		AttributeHelper
				.createOneAttribute(facade, "abc.c", null, "name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.c", (String) null);

		EntityHelper.createEntity("abc", "d");
		AttributeHelper.createOneAttribute(facade, "abc.d", null, "name",
				AttributeType.TEXT, "{\"mandatory\": true, \"minlength\": 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.d", "Johson");

		EntityHelper.createEntity("abc", "e");
		AttributeHelper
				.createOneAttribute(facade, "abc.e", null, "name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.e", (String) null);

		EntityHelper.createEntity("abc", "f");
		AttributeHelper.createOneAttribute(facade, "abc.f", null, "name",
				AttributeType.TEXT, "{\"mandatory\": true, \"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.f", "Johson");

		EntityHelper.createEntity("abc", "g");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.g",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 6, \"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.g", (String) null);

		EntityHelper.createEntity("abc", "i");
		AttributeHelper.createOneAttribute(facade, "abc.i", null, "name",
				AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 6, \"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.i", "Johson");

		EntityHelper.createEntity("abc", "j");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.j",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 3, \"maxlength\" : 8}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.j", (String) null);

		EntityHelper.createEntity("abc", "k");
		AttributeHelper.createOneAttribute(facade, "abc.k", null, "name",
				AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 3, \"maxlength\" : 8}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.k", "Johson");

		EntityHelper.createEntity("abc", "l");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.l",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"abc@abc.com\", \"minlength\": 3, \"maxlength\" : 15, \"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\"}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.l", (String) null);

		EntityHelper.createEntity("abc", "m");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.m",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"minlength\": 3, \"maxlength\" : 15, \"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\"}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.m", "abc@abc.com");
	}

	@Test
	public void instanceWithValidValuesForTheConfigurationOfAttributesLongText() {
		EntityHelper.createEntity("abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "name",
				AttributeType.LONGTEXT, "{\"mandatory\": true}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", "Jose");

		EntityHelper.createEntity("abc", "b");
		AttributeHelper.createOneAttribute(facade, "abc.b", null, "name",
				AttributeType.LONGTEXT, "{\"default\" : \"default longtext\"}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b", (String) null);

		EntityHelper.createEntity("abc", "c");
		AttributeHelper.createOneAttribute(facade, "abc.c", null, "name",
				AttributeType.LONGTEXT, "{\"default\" : \"default longtext\"}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.c", "Jose");

		EntityHelper.createEntity("abc", "d");
		AttributeHelper.createOneAttribute(facade, "abc.d", null, "name",
				AttributeType.LONGTEXT, "{\"minlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.d", "Johson");

		EntityHelper.createEntity("abc", "e");
		AttributeHelper.createOneAttribute(facade, "abc.e", null, "name",
				AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"minlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.e", "Johson");

		EntityHelper.createEntity("abc", "f");
		AttributeHelper
				.createOneAttribute(facade, "abc.f", null, "name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.f", (String) null);

		EntityHelper.createEntity("abc", "g");
		AttributeHelper
				.createOneAttribute(facade, "abc.g", null, "name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.g", "abccab");

		EntityHelper.createEntity("abc", "h");
		AttributeHelper.createOneAttribute(facade, "abc.h", null, "name",
				AttributeType.LONGTEXT, "{\"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.h", (String) null);

		EntityHelper.createEntity("abc", "i");
		AttributeHelper.createOneAttribute(facade, "abc.i", null, "name",
				AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.i", "Johson");

		EntityHelper.createEntity("abc", "j");
		AttributeHelper
				.createOneAttribute(facade, "abc.j", null, "name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.j", (String) null);

		EntityHelper.createEntity("abc", "k");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.k",
						null,
						"name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"minlength\" : 6, \"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.k", (String) null);

		EntityHelper.createEntity("abc", "l");
		AttributeHelper
				.createOneAttribute(
						facade,
						"abc.l",
						null,
						"name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"minlength\" : 6, \"maxlength\" : 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.l", "abccba");
	}

	public void instanceWithValidValuesForTheConfigurationOfAttributesInteger() {
		EntityHelper.createEntity("abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "number",
				AttributeType.INTEGER, "{\"default\": 1}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", (String) null);

		EntityHelper.createEntity("abc", "b");
		AttributeHelper.createOneAttribute(facade, "abc.b", null, "number",
				AttributeType.INTEGER, "{\"minvalue\": 1}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b", "1");

		EntityHelper.createEntity("abc", "c");
		AttributeHelper.createOneAttribute(facade, "abc.c", null, "number",
				AttributeType.INTEGER, "{\"maxvalue\": 1}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.c", "1");

		EntityHelper.createEntity("abc", "d");
		AttributeHelper.createOneAttribute(facade, "abc.d", null, "number",
				AttributeType.INTEGER, "{\"minvalue\": 1, \"maxvalue\": 1}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.d", "1");

		EntityHelper.createEntity("abc", "e");
		AttributeHelper.createOneAttribute(facade, "abc.e", null, "number",
				AttributeType.INTEGER,
				"{\"default\": 1, \"minvalue\": 1, \"maxvalue\": 1}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.e", (String) null);

		EntityHelper.createEntity("abc", "f");
		AttributeHelper
				.createOneAttribute(facade, "abc.f", null, "number",
						AttributeType.INTEGER,
						"{\"mandatory\": true,\"default\": 1, \"minvalue\": 1, \"maxvalue\": 1}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.f", (String) null);
	}

	public void instanceWithValidValuesForTheConfigurationOfAttributesPassword() {
		EntityHelper.createEntity("abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "secretKey",
				AttributeType.PASSWORD, "{\"default\": \"password\"}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.a", (String) null);

		EntityHelper.createEntity("abc", "b");
		AttributeHelper.createOneAttribute(facade, "abc.b", null, "secretKey",
				AttributeType.PASSWORD, "{\"minUppers\": 2}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.b", "SecretKey");

		EntityHelper.createEntity("abc", "c");
		AttributeHelper.createOneAttribute(facade, "abc.c", null, "secretKey",
				AttributeType.PASSWORD, "{\"minNumbers\": 2}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.c", "1secretkey2");

		EntityHelper.createEntity("abc", "d");
		AttributeHelper.createOneAttribute(facade, "abc.d", null, "secretKey",
				AttributeType.PASSWORD, "{\"minSymbols\": 2}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.d", "&secretkey%");

		EntityHelper.createEntity("abc", "e");
		AttributeHelper.createOneAttribute(facade, "abc.e", null, "secretKey",
				AttributeType.PASSWORD, "{\"maxRepeat\": 2}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.e", "seecretkey");

		EntityHelper.createEntity("abc", "f");
		AttributeHelper.createOneAttribute(facade, "abc.f", null, "secretKey",
				AttributeType.PASSWORD, "{\"minlength\": 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.f", "secret");

		EntityHelper.createEntity("abc", "g");
		AttributeHelper.createOneAttribute(facade, "abc.g", null, "secretKey",
				AttributeType.PASSWORD, "{\"maxlength\": 6}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.g", "secret");

		EntityHelper.createEntity("abc", "h");
		AttributeHelper.createOneAttribute(facade, "abc.h", null, "secretKey",
				AttributeType.PASSWORD, "{\"mandatory\": true}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.h", "secret");

		EntityHelper.createEntity("abc", "i");
		AttributeHelper.createOneAttribute(facade, "abc.i", null, "secretKey",
				AttributeType.PASSWORD, "{\"mandatory\": false}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.h", (String) null);

		EntityHelper.createEntity("abc", "j");
		AttributeHelper.createOneAttribute(facade, "abc.j", null, "secretKey",
				AttributeType.PASSWORD,
				"{\"mandatory\": true, \"default\": \"mypassword\"}");
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.j", (String) null);

		EntityHelper.createEntity("abc", "k");
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
		InstanceHelper.createAndVerifyOneInstance(facade, "abc.k", "0tH3r@ss");
	}

	@Test
	public void instanceWithInvalidValuesForIntegerTypeAttributeWithoutConfiguration() {
		String messageException = "Invalid value for the Instance. The value for the 'name' attribute must be an int";

		EntityHelper.createEntity("abc", "a");
		AttributeHelper.createOneAttribute(facade, "abc.a", null, "name",
				AttributeType.INTEGER, "");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, "false");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, "true");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, "pa");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, "3.2");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, "0.75");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(facade, "abc.a",
				messageException, "-3.2");
	}

	@Test
	public void instanceWithInvalidValuesForConfigurationOfTextAttributes() {
		EntityHelper.createEntity("abc", "a");
		EntityHelper.createEntity("abc", "b");
		EntityHelper.createEntity("abc", "ba");
		EntityHelper.createEntity("abc", "c");
		EntityHelper.createEntity("abc", "ca");
		EntityHelper.createEntity("abc", "d");
		EntityHelper.createEntity("abc", "da");

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
						"abc.b",
						null,
						"nameB",
						AttributeType.TEXT,
						"{\"minlength\" : 5}",
						"abcd",
						"Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.ba",
						null,
						"nameBA",
						AttributeType.TEXT,
						"{\"mandatory\" : true, \"minlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, The value for 'nameBA' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.c",
						null,
						"nameC",
						AttributeType.TEXT,
						"{\"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.ca",
						null,
						"nameCA",
						AttributeType.TEXT,
						"{\"mandatory\" : true, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.d",
						null,
						"nameD",
						AttributeType.TEXT,
						"{\"minlength\" : 5, \"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameD' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.da",
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
	public void instanceWithInvalidValuesForConfigurationOfLongTextAttributes() {
		EntityHelper.createEntity("abc", "a");
		EntityHelper.createEntity("abc", "b");
		EntityHelper.createEntity("abc", "ba");
		EntityHelper.createEntity("abc", "c");
		EntityHelper.createEntity("abc", "ca");
		EntityHelper.createEntity("abc", "d");
		EntityHelper.createEntity("abc", "e");

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
						"abc.b",
						null,
						"nameB",
						AttributeType.LONGTEXT,
						"{\"minlength\" : 5}",
						"abcd",
						"Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.ba",
						null,
						"nameBA",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"minlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, The value for 'nameBA' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.c",
						null,
						"nameC",
						AttributeType.LONGTEXT,
						"{\"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.ca",
						null,
						"nameCA",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.d",
						null,
						"nameD",
						AttributeType.LONGTEXT,
						"{\"minlength\" : 5, \"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameD' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.e",
						null,
						"nameE",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameE' attribute is mandatory, The value for 'nameE' must have a minimum length of 5 characters");
	}

	@Test
	public void instanceWithInvalidValuesForConfigurationOfIntegerAttributes() {
		EntityHelper.createEntity("abc", "a");
		EntityHelper.createEntity("abc", "b");
		EntityHelper.createEntity("abc", "c");
		EntityHelper.createEntity("abc", "d");
		EntityHelper.createEntity("abc", "e");
		EntityHelper.createEntity("abc", "f");
		EntityHelper.createEntity("abc", "g");

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
						"abc.b",
						null,
						"nameB",
						AttributeType.INTEGER,
						"{\"minvalue\" : 3}",
						"2",
						"Invalid value for the Instance. The value for 'nameB' must be greater or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.c",
						null,
						"nameC",
						AttributeType.INTEGER,
						"{\"mandatory\" : true, \"minvalue\" : 3}",
						null,
						"Invalid value for the Instance. The value for the 'nameC' attribute is mandatory, The value for 'nameC' must be greater or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.d",
						null,
						"nameD",
						AttributeType.INTEGER,
						"{\"maxvalue\" : 3}",
						"4",
						"Invalid value for the Instance. The value for 'nameD' must be smaller or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.e",
						null,
						"nameE",
						AttributeType.INTEGER,
						"{\"mandatory\" : true, \"maxvalue\" : 3}",
						null,
						"Invalid value for the Instance. The value for the 'nameE' attribute is mandatory, The value for 'nameE' must be smaller or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.f",
						null,
						"nameF",
						AttributeType.INTEGER,
						"{\"minvalue\" : 3, \"maxvalue\" : 3}",
						"4",
						"Invalid value for the Instance. The value for 'nameF' must be smaller or equal to 3");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.g",
						null,
						"nameG",
						AttributeType.INTEGER,
						"{\"mandatory\" : true, \"minvalue\" : 3, \"maxvalue\" : 3}",
						null,
						"Invalid value for the Instance. The value for the 'nameG' attribute is mandatory, The value for 'nameG' must be greater or equal to 3, The value for 'nameG' must be smaller or equal to 3");
	}

	@Test
	public void instanceWithInvalidValuesForTheConfigurationOfAttributesPassword() {
		EntityHelper.createEntity("abc", "a");
		EntityHelper.createEntity("abc", "b");
		EntityHelper.createEntity("abc", "ba");
		EntityHelper.createEntity("abc", "c");
		EntityHelper.createEntity("abc", "ca");
		EntityHelper.createEntity("abc", "d");
		EntityHelper.createEntity("abc", "da");
		EntityHelper.createEntity("abc", "e");
		EntityHelper.createEntity("abc", "f");
		EntityHelper.createEntity("abc", "g");
		EntityHelper.createEntity("abc", "h");
		EntityHelper.createEntity("abc", "i");
		EntityHelper.createEntity("abc", "j");

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
						"abc.b",
						null,
						"nameB",
						AttributeType.PASSWORD,
						"{\"minlength\" : 5}",
						"abcd",
						"Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.ba",
						null,
						"nameBA",
						AttributeType.PASSWORD,
						"{\"mandatory\" : true, \"minlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, The value for 'nameBA' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.c",
						null,
						"nameC",
						AttributeType.PASSWORD,
						"{\"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.ca",
						null,
						"nameCA",
						AttributeType.PASSWORD,
						"{\"mandatory\" : true, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.d",
						null,
						"nameD",
						AttributeType.PASSWORD,
						"{\"minlength\" : 5, \"maxlength\" : 5}",
						"abcdef",
						"Invalid value for the Instance. The value for 'nameD' must have a maximum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.da",
						null,
						"nameDA",
						AttributeType.PASSWORD,
						"{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}",
						"",
						"Invalid value for the Instance. The value for the 'nameDA' attribute is mandatory, The value for 'nameDA' must have a minimum length of 5 characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.e",
						null,
						"nameE",
						AttributeType.PASSWORD,
						"{\"minUppers\" : 3}",
						"ABcdef",
						"Invalid value for the Instance. The value for 'nameE' must have at least 3 uppercase characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.f",
						null,
						"nameF",
						AttributeType.PASSWORD,
						"{\"minNumbers\" : 3}",
						"abc12def",
						"Invalid value for the Instance. The value for 'nameF' must be at least 3 numbers");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.g",
						null,
						"nameG",
						AttributeType.PASSWORD,
						"{\"minSymbols\" : 3}",
						"ab%c12def*",
						"Invalid value for the Instance. The value for 'nameG' must have at least 3 symbol characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.h",
						null,
						"nameH",
						AttributeType.PASSWORD,
						"{\"maxRepeat\" : 0}",
						"ab%c1a2e*",
						"Invalid value for the Instance. The value for 'nameH' must not have repeated characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.i",
						null,
						"nameI",
						AttributeType.PASSWORD,
						"{\"maxRepeat\" : 1}",
						"ab%ac1a2e*",
						"Invalid value for the Instance. The value for 'nameI' must not have more than 2 repeated characters");
		InstanceHelper
				.invalidValueForInstance(
						facade,
						"abc.j",
						null,
						"nameJ",
						AttributeType.PASSWORD,
						"{\"mandatory\": true, \"minlength\": 4, \"maxlength\": 4, \"minUppers\" : 3, \"minNumbers\" : 2, \"minSymbols\" : 1, \"maxRepeat\" : 0}",
						"ab1CfdeFa",
						"Invalid value for the Instance. The value for 'nameJ' must have a maximum length of 4 characters, The value for 'nameJ' must have at least 3 uppercase characters, The value for 'nameJ' must be at least 2 numbers, The value for 'nameJ' must have at least 1 symbol character, The value for 'nameJ' must not have repeated characters");

	}
}