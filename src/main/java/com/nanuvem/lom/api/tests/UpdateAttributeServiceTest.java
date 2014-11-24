package com.nanuvem.lom.api.tests;

import static com.nanuvem.lom.api.tests.AttributeHelper.LONGTEXT;
import static com.nanuvem.lom.api.tests.AttributeHelper.PASSWORD;
import static com.nanuvem.lom.api.tests.AttributeHelper.TEXT;
import static com.nanuvem.lom.api.tests.AttributeHelper.createOneAttribute;
import static com.nanuvem.lom.api.tests.AttributeHelper.expectExceptionOnUpdateInvalidAttribute;
import static com.nanuvem.lom.api.tests.AttributeHelper.updateAttribute;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.api.Attribute;
import com.nanuvem.lom.api.Facade;

public abstract class UpdateAttributeServiceTest {

	private static final String TEXT_CONFIGURATION_PARTIAL = "{\"minlength\": 5,\"maxlength\": 15}";

	private static final String TEXT_CONFIGURATION_COMPLETE = "{\"mandatory\": false, \"default\": \"abc@abc.com\", "
			+ "\"regex\": \"(\\\\w[-._\\\\w]\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\", "
			+ "\"minlength\": 5,\"maxlength\": 15}";

	private static final String LONGTEXT_CONFIGURATION_PARTIAL = "{\"minlength\": 5,\"maxlength\": 15}";

	private static final String LONGTEXT_CONFIGURATION_COMPLETE = "{\"mandatory\": false, \"default\": \"long text\", "
			+ "\"minlength\": 5,\"maxlength\": 15}";

	private static final String PASSWORD_CONFIGURATION_PARTIAL = "{\"minlength\": 5,\"maxlength\": 15}";

	private static final String PASSWORD_CONFIGURATION_COMPLETE = "{\"mandatory\": false, "
			+ "\"default\": \"Abc12@abd.com\", "
			+ "\"minUppers\": 1, "
			+ "\"minNumbers\": 2, "
			+ "\"minSymbols\": 1, "
			+ "\"maxRepeat\": 1," + "\"minlength\": 5," + "\"maxlength\": 15}";

	private static final String CONFIGURATION_MANDATORY_TRUE = "{\"mandatory\":true}";

	private Facade facade;

	public abstract Facade createFacade();

	@Before
	public void init() {
		facade = createFacade();
		EntityHelper.setFacade(facade);
	}

	@Test
	public void validNewName() {
		EntityHelper.createEntity("abc", "a");
		Attribute createdAttribute = createOneAttribute(facade,
				"abc.a", null, "pa", LONGTEXT, null);

		Attribute updatedAttribute = updateAttribute(facade, "abc.a",
				createdAttribute, 1, "pb", LONGTEXT, null);
		verifyUpdatedAttribute(createdAttribute, updatedAttribute);
	}

	@Test
	public void invalidNewName() {
		EntityHelper.createEntity("abc", "a");
		Attribute createdAttribute = createOneAttribute(facade,
				"abc.a", null, "pa", LONGTEXT, null);
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "",
				"The name of an Attribute is mandatory");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, null,
				"The name of an Attribute is mandatory");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "p a",
				"Invalid value for Attribute name: p a");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a$",
				"Invalid value for Attribute name: a$");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a#",
				"Invalid value for Attribute name: a#");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a=",
				"Invalid value for Attribute name: a=");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a'",
				"Invalid value for Attribute name: a'");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a.",
				"Invalid value for Attribute name: a.");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a.a",
				"Invalid value for Attribute name: a.a");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a/a",
				"Invalid value for Attribute name: a/a");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a*",
				"Invalid value for Attribute name: a*");
	}

	@Test
	public void validNewSequence() {
		EntityHelper.createEntity("abc", "a");
		Attribute createdAttribute1 = createOneAttribute(facade,
				"abc.a", null, "pa", TEXT, null);
		Attribute createdAttribute2 = createOneAttribute(facade,
				"abc.a", null, "pb", TEXT, null);

		Attribute updatedAttribute1 = updateAttribute(facade,
				"abc.a", createdAttribute1, 2, "pa", TEXT, null);
		verifyUpdatedAttribute(createdAttribute1, updatedAttribute1);

		Attribute updatedAttribute2 = updateAttribute(facade,
				"abc.a", createdAttribute2, 2, "pb", TEXT, null);
		verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);

		Attribute updatedAttribute3 = updateAttribute(facade,
				"abc.a", updatedAttribute2, 1, "pb", TEXT, null);
		verifyUpdatedAttribute(updatedAttribute2, updatedAttribute3);
	}

	@Test
	public void invalidNewSequence() {
		EntityHelper.createEntity("abc", "a");

		Attribute createdAttribute = createOneAttribute(facade,
				"abc.a", null, "pa", TEXT, null);

		createOneAttribute(facade, "abc.a", null, "pb", TEXT, null);

		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, null, "pa", LONGTEXT, null,
				"Invalid value for Attribute sequence: null");
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, -1, "pa", LONGTEXT, null,
				"Invalid value for Attribute sequence: -1");
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 0, "pa", LONGTEXT, null,
				"Invalid value for Attribute sequence: 0");
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 3, "pa", LONGTEXT, null,
				"Invalid value for Attribute sequence: 3");
	}

	@Test
	public void changeType() {
		EntityHelper.createEntity("abc", "a");
		Attribute createdAttribute = createOneAttribute(facade,
				"abc.a", null, "pa", TEXT, null);

		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 1, "pa", null, null,
				"Can not change the type of an attribute");
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 1, "pa", LONGTEXT, null,
				"Can not change the type of an attribute");
	}

	@Test
	public void renamingConflicts() {
		EntityHelper.createEntity("abc", "a");
		EntityHelper.createEntity("abc", "b");

		createOneAttribute(facade, "abc.a", null, "pa", TEXT, null);

		Attribute createdAttribute2 = createOneAttribute(facade,
				"abc.b", null, "pb", TEXT, null);
		Attribute createdAttribute3 = createOneAttribute(facade,
				"abc.a", null, "pc", TEXT, null);

		Attribute updatedAttribute2 = updateAttribute(facade,
				"abc.b", createdAttribute2, 1, "pa", TEXT, null);
		verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);

		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute3, 1, "pa", TEXT, null,
				"Attribute duplication on abc.a Entity. It already has an attribute pa.");

		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute3, 1, "pA", TEXT, null,
				"Attribute duplication on abc.a Entity. It already has an attribute pa.");

		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute3, 1, "PA", TEXT, null,
				"Attribute duplication on abc.a Entity. It already has an attribute pa.");
	}

	@Test
	public void genericChangeConfiguration() {
		EntityHelper.createEntity("abc", "a");

		Attribute createdAttribute1 = createOneAttribute(facade,
				"abc.a", null, "pa", TEXT, CONFIGURATION_MANDATORY_TRUE);

		Attribute createdAttribute2 = createOneAttribute(facade,
				"abc.a", null, "pb", TEXT, null);

		Attribute updatedAttribute11 = updateAttribute(facade,
				"abc.a", createdAttribute1, 1, "pa", TEXT,
				"{\"mandatory\":false}");
		verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);

		Attribute updatedAttribute12 = updateAttribute(facade,
				"abc.a", updatedAttribute11, 2, "pa", TEXT, null);
		verifyUpdatedAttribute(updatedAttribute11, updatedAttribute12);

		Attribute updatedAttribute2 = updateAttribute(facade,
				"abc.a", createdAttribute2, 2, "pb", TEXT,
				CONFIGURATION_MANDATORY_TRUE);
		verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);
	}

	@Test
	public void validChangeConfigurationForTextAttributeType() {
		EntityHelper.createEntity("abc", "a");

		Attribute createdAttribute1 = createOneAttribute(facade,
				"abc.a", null, "pa", TEXT, CONFIGURATION_MANDATORY_TRUE);
		Attribute createdAttribute2 = createOneAttribute(facade,
				"abc.a", null, "pb", TEXT, TEXT_CONFIGURATION_COMPLETE);

		Attribute updatedAttribute11 = updateAttribute(facade,
				"abc.a", createdAttribute1, 1, "pa", TEXT,
				TEXT_CONFIGURATION_PARTIAL);
		verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);

		Attribute updatedAttribute12 = updateAttribute(facade,
				"abc.a", updatedAttribute11, 1, "pa", TEXT,
				TEXT_CONFIGURATION_COMPLETE);
		verifyUpdatedAttribute(updatedAttribute11, updatedAttribute12);

		Attribute updatedAttribute21 = updateAttribute(facade,
				"abc.a", createdAttribute2, 2, "pb", TEXT,
				TEXT_CONFIGURATION_PARTIAL);
		verifyUpdatedAttribute(createdAttribute2, updatedAttribute21);

		Attribute updatedAttribute22 = updateAttribute(facade,
				"abc.a", updatedAttribute21, 2, "pb", TEXT,
				CONFIGURATION_MANDATORY_TRUE);
		verifyUpdatedAttribute(updatedAttribute21, updatedAttribute22);

		Attribute updatedAttribute23 = updateAttribute(facade,
				"abc.a", updatedAttribute22, 2, "pb", TEXT,
				"{\"default\":\"abc\"}");
		verifyUpdatedAttribute(updatedAttribute22, updatedAttribute23);

		Attribute updatedAttribute24 = updateAttribute(facade,
				"abc.a", updatedAttribute23, 2, "pb", TEXT,
				"{\"default\":\"123\"}");
		verifyUpdatedAttribute(updatedAttribute23, updatedAttribute24);
	}

	@Test
	public void invalidChangeConfigurationForTextAttributeType() {
		EntityHelper.createEntity("abc", "a");
		Attribute createdAttribute = createOneAttribute(facade,
				"abc.a", null, "pa", TEXT, null);

		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"mandatory\":10}",
				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"mandatory\":\"true\"}",
				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 1, "pa", TEXT, "{\"default\":10}",
				"Invalid configuration for attribute pa: the default value must be a string");
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 1, "pa", TEXT, "{\"regex\":10}",
				"Invalid configuration for attribute pa: the regex value must be a string");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"minlength\":\"abc\"}",
				"Invalid configuration for attribute pa: the minlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"minlength\":10.0}",
				"Invalid configuration for attribute pa: the minlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"maxlength\":\"abc\"}",
				"Invalid configuration for attribute pa: the maxlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"maxlength\":10.0}",
				"Invalid configuration for attribute pa: the maxlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"default\":\"abc\", \"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\"}",
				"Invalid configuration for attribute pa: the default value does not match regex configuration");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"default\":\"abc\", \"minlength\":5}",
				"Invalid configuration for attribute pa: the default value is smaller than minlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"default\":\"abcabc\", \"maxlength\":5}",
				"Invalid configuration for attribute pa: the default value is greater than maxlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"minlength\":50, \"maxlength\":10}",
				"Invalid configuration for attribute pa: the minlength is greater than maxlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
				"Invalid configuration for attribute pa: the default value is smaller than minlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				TEXT,
				"{\"anyconf\":\"abc\"}",
				"Invalid configuration for attribute pa: the anyconf configuration attribute is unknown");
	}

	@Test
	public void validChangeConfigurationForLongTextAttributeType() {
		EntityHelper.createEntity("abc", "a");

		Attribute createdAttribute1 = createOneAttribute(facade,
				"abc.a", null, "pa", LONGTEXT, CONFIGURATION_MANDATORY_TRUE);
		Attribute createdAttribute2 = createOneAttribute(facade,
				"abc.a", null, "pb", LONGTEXT, LONGTEXT_CONFIGURATION_COMPLETE);

		Attribute updatedAttribute11 = updateAttribute(facade,
				"abc.a", createdAttribute1, 1, "pa", LONGTEXT,
				LONGTEXT_CONFIGURATION_PARTIAL);
		verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);

		Attribute updatedAttribute12 = updateAttribute(facade,
				"abc.a", updatedAttribute11, 1, "pa", LONGTEXT,
				LONGTEXT_CONFIGURATION_COMPLETE);
		verifyUpdatedAttribute(updatedAttribute11, updatedAttribute12);

		Attribute updatedAttribute21 = updateAttribute(facade,
				"abc.a", createdAttribute2, 2, "pb", LONGTEXT,
				LONGTEXT_CONFIGURATION_PARTIAL);
		verifyUpdatedAttribute(createdAttribute2, updatedAttribute21);

		Attribute updatedAttribute22 = updateAttribute(facade,
				"abc.a", updatedAttribute21, 2, "pb", LONGTEXT,
				CONFIGURATION_MANDATORY_TRUE);
		verifyUpdatedAttribute(updatedAttribute21, updatedAttribute22);

		Attribute updatedAttribute23 = updateAttribute(facade,
				"abc.a", updatedAttribute22, 2, "pb", LONGTEXT,
				"{\"default\":\"abc\"}");
		verifyUpdatedAttribute(updatedAttribute22, updatedAttribute23);

		Attribute updatedAttribute24 = updateAttribute(facade,
				"abc.a", updatedAttribute23, 2, "pb", LONGTEXT,
				"{\"default\":\"123\"}");
		verifyUpdatedAttribute(updatedAttribute23, updatedAttribute24);

	}

	@Test
	public void invalidChangeConfigurationForLongTextAttributeType() {
		EntityHelper.createEntity("abc", "a");
		Attribute createdAttribute = createOneAttribute(facade,
				"abc.a", null, "pa", LONGTEXT, null);

		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"mandatory\":10}",
				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"mandatory\":\"true\"}",
				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 1, "pa", LONGTEXT, "{\"default\":10}",
				"Invalid configuration for attribute pa: the default value must be a string");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"minlength\":\"abc\"}",
				"Invalid configuration for attribute pa: the minlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"minlength\":10.0}",
				"Invalid configuration for attribute pa: the minlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"maxlength\":\"abc\"}",
				"Invalid configuration for attribute pa: the maxlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"maxlength\":10.0}",
				"Invalid configuration for attribute pa: the maxlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"default\":\"abc\", \"minlength\":5}",
				"Invalid configuration for attribute pa: the default value is smaller than minlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"default\":\"abcabc\", \"maxlength\":5}",
				"Invalid configuration for attribute pa: the default value is greater than maxlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"minlength\":50, \"maxlength\":10}",
				"Invalid configuration for attribute pa: the minlength is greater than maxlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
				"Invalid configuration for attribute pa: the default value is smaller than minlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				LONGTEXT,
				"{\"anyconf\":\"abc\"}",
				"Invalid configuration for attribute pa: the anyconf configuration attribute is unknown");
	}

	@Test
	public void validChangeConfigurationForPasswordAttributeType() {
		EntityHelper.createEntity("abc", "a");

		Attribute createdAttribute1 = createOneAttribute(facade,
				"abc.a", null, "pa", PASSWORD, CONFIGURATION_MANDATORY_TRUE);
		Attribute createdAttribute2 = createOneAttribute(facade,
				"abc.a", null, "pb", PASSWORD, PASSWORD_CONFIGURATION_COMPLETE);

		Attribute updatedAttribute11 = updateAttribute(facade,
				"abc.a", createdAttribute1, 1, "pa", PASSWORD,
				PASSWORD_CONFIGURATION_PARTIAL);
		verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);

		Attribute updatedAttribute12 = updateAttribute(facade,
				"abc.a", updatedAttribute11, 1, "pa", PASSWORD,
				PASSWORD_CONFIGURATION_COMPLETE);
		verifyUpdatedAttribute(updatedAttribute11, updatedAttribute12);

		Attribute updatedAttribute21 = updateAttribute(facade,
				"abc.a", createdAttribute2, 2, "pb", PASSWORD,
				PASSWORD_CONFIGURATION_PARTIAL);
		verifyUpdatedAttribute(createdAttribute2, updatedAttribute21);

		Attribute updatedAttribute22 = updateAttribute(facade,
				"abc.a", updatedAttribute21, 2, "pb", PASSWORD,
				CONFIGURATION_MANDATORY_TRUE);
		verifyUpdatedAttribute(updatedAttribute21, updatedAttribute22);

		Attribute updatedAttribute23 = updateAttribute(facade,
				"abc.a", updatedAttribute22, 2, "pb", PASSWORD,
				"{\"default\":\"abc\"}");
		verifyUpdatedAttribute(updatedAttribute22, updatedAttribute23);

		Attribute updatedAttribute24 = updateAttribute(facade,
				"abc.a", updatedAttribute23, 2, "pb", PASSWORD,
				"{\"default\":\"123\"}");
		verifyUpdatedAttribute(updatedAttribute23, updatedAttribute24);
	}

	@Test
	public void invalidChangeConfigurationForPaswordAttributeType() {
		EntityHelper.createEntity("abc", "a");
		Attribute createdAttribute = createOneAttribute(facade,
				"abc.a", null, "pa", PASSWORD, null);

		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"mandatory\":10}",
				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"mandatory\":\"true\"}",
				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 1, "pa", PASSWORD, "{\"default\":10}",
				"Invalid configuration for attribute pa: the default value must be a string");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minlength\":\"abc\"}",
				"Invalid configuration for attribute pa: the minlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minlength\":10.0}",
				"Invalid configuration for attribute pa: the minlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"maxlength\":\"abc\"}",
				"Invalid configuration for attribute pa: the maxlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"maxlength\":10.0}",
				"Invalid configuration for attribute pa: the maxlength value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abc\", \"minlength\":5}",
				"Invalid configuration for attribute pa: the default value is smaller than minlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abcabc\", \"maxlength\":5}",
				"Invalid configuration for attribute pa: the default value is greater than maxlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minlength\":50, \"maxlength\":10}",
				"Invalid configuration for attribute pa: the minlength is greater than maxlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
				"Invalid configuration for attribute pa: the default value is smaller than minlength");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minUppers\":\"abc\"}",
				"Invalid configuration for attribute pa: the minUppers value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minUppers\":10.0}",
				"Invalid configuration for attribute pa: the minUppers value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minNumbers\":\"abc\"}",
				"Invalid configuration for attribute pa: the minNumbers value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minNumbers\":10.0}",
				"Invalid configuration for attribute pa: the minNumbers value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minSymbols\":\"abc\"}",
				"Invalid configuration for attribute pa: the minSymbols value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"minSymbols\":10.0}",
				"Invalid configuration for attribute pa: the minSymbols value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"maxRepeat\":\"abc\"}",
				"Invalid configuration for attribute pa: the maxRepeat value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"maxRepeat\":10.0}",
				"Invalid configuration for attribute pa: the maxRepeat value must be an integer number");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abcdef\", \"minUppers\":1}",
				"Invalid configuration for attribute pa: the default value must have at least 1 upper case character");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abcDef\", \"minUppers\":2}",
				"Invalid configuration for attribute pa: the default value must have at least 2 upper case characters");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abcdef\", \"minNumbers\":1}",
				"Invalid configuration for attribute pa: the default value must have at least 1 numerical character");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abc1\", \"minNumbers\":2}",
				"Invalid configuration for attribute pa: the default value must have at least 2 numerical characters");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abcdef\", \"minSymbols\":1}",
				"Invalid configuration for attribute pa: the default value must have at least 1 symbol character");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"!abc\", \"minSymbols\":2}",
				"Invalid configuration for attribute pa: the default value must have at least 2 symbol characters");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"aabcdef\", \"maxRepeat\":0}",
				"Invalid configuration for attribute pa: the default value must not have repeated characters");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abcccd\", \"maxRepeat\":1}",
				"Invalid configuration for attribute pa: the default value must not have more than 2 repeated characters");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"Abcdef12!@b\", \"minUppers\":1, \"minNumbers\":2, \"minSymbols\":3, \"maxRepeat\":1}",
				"Invalid configuration for attribute pa: the default value must have at least 3 symbol characters");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"default\":\"abccc\", \"minUppers\":1, \"minNumbers\":2, \"minSymbols\":3, \"maxRepeat\":1}",
				"Invalid configuration for attribute pa: the default value must have at least 1 upper case "
						+ "character, the default value must have at least 2 numerical characters, the default value "
						+ "must have at least 3 symbol characters, the default value must not have more than 2 "
						+ "repeated characters");
		expectExceptionOnUpdateInvalidAttribute(
				facade,
				"abc.a",
				createdAttribute,
				1,
				"pa",
				PASSWORD,
				"{\"anyconf\":\"abc\"}",
				"Invalid configuration for attribute pa: the anyconf configuration attribute is unknown");
	}

	private void expectExceptionOnUpdateWithInvalidNewName(
			Attribute createdAttribute, String invalidNewName,
			String exceptedMessage) {
		expectExceptionOnUpdateInvalidAttribute(facade, "abc.a",
				createdAttribute, 1, invalidNewName, LONGTEXT, null,
				exceptedMessage);
	}

	private void verifyUpdatedAttribute(Attribute attributeBeforeUpgrade,
			Attribute updatedAttribute) {

		Assert.assertNotNull("updatedAttribute.id should not be null",
				updatedAttribute.getId());

		int versionIncrementedInCreateAttribute = attributeBeforeUpgrade
				.getVersion() + 1;
		Assert.assertEquals("updatedAttribute.version should be "
				+ versionIncrementedInCreateAttribute,
				versionIncrementedInCreateAttribute,
				(int) updatedAttribute.getVersion());

		Attribute listedAttribute = facade
				.findAttributeById(attributeBeforeUpgrade.getId());
		Assert.assertEquals("listedAttribute should be to updatedAttribute",
				updatedAttribute, listedAttribute);

		Assert.assertFalse("listedAttribute should be to createdAttribute",
				attributeBeforeUpgrade.equals(listedAttribute));
	}
}