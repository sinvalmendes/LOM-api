package com.nanuvem.lom.api.tests.attribute;

import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.MANDATORY_TRUE;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.TEXT;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.createAndVerifyOneAttribute;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.createOneAttribute;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.expectExceptionOnCreateInvalidAttribute;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.expectExceptionOnUpdateInvalidAttribute;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.updateAttribute;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.verifyUpdatedAttribute;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.createEntity;

import org.junit.Test;

import com.nanuvem.lom.api.Attribute;
import com.nanuvem.lom.api.tests.LomTestCase;

public abstract class TextAttributeTest extends LomTestCase {

    private static final String TEXT_CONFIGURATION_PARTIAL = "{\"minlength\": 5,\"maxlength\": 15}";

    private static final String TEXT_CONFIGURATION_COMPLETE = "{\"mandatory\": false, \"default\": \"abc@abc.com\", "
            + "\"regex\": \"(\\\\w[-._\\\\w]\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\", "
            + "\"minlength\": 5,\"maxlength\": 15}";


    @Test
    public void validateConfigurationForTextAttributeType() {
        createEntity("abc", "a");

        createAndVerifyOneAttribute("abc.a", 1, "pa", TEXT,
                "{\"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]\\\\w.\\\\w{2,3})\"}");

        createAndVerifyOneAttribute("abc.a", 1, "pb", TEXT, "{\"minlength\":10}");

        createAndVerifyOneAttribute("abc.a", 1, "pc", TEXT, "{\"minlength\":100}");

        createAndVerifyOneAttribute("abc.a", 1, "pd", TEXT, "{\"mandatory\": true, \"regex\": "
                + "\"(\\\\w[-._\\\\w]\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\", "
                + "\"minlength\": 5,\"maxlength\": 15}");

        createAndVerifyOneAttribute("abc.a", 1, "pe", TEXT, "");

        createAndVerifyOneAttribute("abc.a", 1, "pf", TEXT,
                "{\"default\": \"abc@abc.com\",\"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\","
                        + "\"minlength\": 5,\"maxlength\": 15}");

        createAndVerifyOneAttribute("abc.a", 1, "pg", TEXT, "{\"default\":\"abc\"}");

    }

    @Test
    public void invalidConfigurationForTextAttributeType() {
        createEntity("abc", "a");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "{\"mandatory\":10}",
                "Invalid configuration for attribute pa: the mandatory value must be true or false literals");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "{\"mandatory\":\"true\"}",
                "Invalid configuration for attribute pa: the mandatory value must be true or false literals");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "{\"default\":10}",
                "Invalid configuration for attribute pa: the default value must be a string");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "{\"regex\":10}",
                "Invalid configuration for attribute pa: the regex value must be a string");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "{\"minlength\":\"abc\"}",
                "Invalid configuration for attribute pa: the minlength value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "{\"minlength\":10.0}",
                "Invalid configuration for attribute pa: the minlength value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "{\"maxlength\":\"abc\"}",
                "Invalid configuration for attribute pa: the maxlength value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT, "{\"maxlength\":10.0}",
                "Invalid configuration for attribute pa: the maxlength value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT,
                "{\"default\":\"abc\", \"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\"}",
                "Invalid configuration for attribute pa: the default value does not match regex configuration");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT,
                "{\"default\":\"abc\", \"minlength\":5}",
                "Invalid configuration for attribute pa: the default value is smaller than minlength");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT,
                "{\"default\":\"abcabc\", \"maxlength\":5}",
                "Invalid configuration for attribute pa: the default value is greater than maxlength");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT,
                "{\"minlength\":50, \"maxlength\":10}",
                "Invalid configuration for attribute pa: the minlength is greater than maxlength");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", TEXT,
                "{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
                "Invalid configuration for attribute pa: the default value is smaller than minlength");
    }

    @Test
    public void validChangeConfigurationForTextAttributeType() {
        createEntity("abc", "a");

        Attribute createdAttribute1 = createOneAttribute("abc.a", null, "pa", TEXT, MANDATORY_TRUE);
        Attribute createdAttribute2 = createOneAttribute("abc.a", null, "pb", TEXT, TEXT_CONFIGURATION_COMPLETE);

        Attribute updatedAttribute11 = updateAttribute("abc.a", createdAttribute1, 1, "pa", TEXT,
                TEXT_CONFIGURATION_PARTIAL);
        verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);

        Attribute updatedAttribute12 = updateAttribute("abc.a", updatedAttribute11, 1, "pa", TEXT,
                TEXT_CONFIGURATION_COMPLETE);
        verifyUpdatedAttribute(updatedAttribute11, updatedAttribute12);

        Attribute updatedAttribute21 = updateAttribute("abc.a", createdAttribute2, 2, "pb", TEXT,
                TEXT_CONFIGURATION_PARTIAL);
        verifyUpdatedAttribute(createdAttribute2, updatedAttribute21);

        Attribute updatedAttribute22 = updateAttribute("abc.a", updatedAttribute21, 2, "pb", TEXT,
                MANDATORY_TRUE);
        verifyUpdatedAttribute(updatedAttribute21, updatedAttribute22);

        Attribute updatedAttribute23 = updateAttribute("abc.a", updatedAttribute22, 2, "pb", TEXT,
                "{\"default\":\"abc\"}");
        verifyUpdatedAttribute(updatedAttribute22, updatedAttribute23);

        Attribute updatedAttribute24 = updateAttribute("abc.a", updatedAttribute23, 2, "pb", TEXT,
                "{\"default\":\"123\"}");
        verifyUpdatedAttribute(updatedAttribute23, updatedAttribute24);
    }

    @Test
    public void invalidChangeConfigurationForTextAttributeType() {
        createEntity("abc", "a");
        Attribute createdAttribute = createOneAttribute("abc.a", null, "pa", TEXT, null);

        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"mandatory\":10}",
                "Invalid configuration for attribute pa: the mandatory value must be true or false literals");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"mandatory\":\"true\"}",
                "Invalid configuration for attribute pa: the mandatory value must be true or false literals");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"default\":10}",
                "Invalid configuration for attribute pa: the default value must be a string");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"regex\":10}",
                "Invalid configuration for attribute pa: the regex value must be a string");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"minlength\":\"abc\"}",
                "Invalid configuration for attribute pa: the minlength value must be an integer number");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"minlength\":10.0}",
                "Invalid configuration for attribute pa: the minlength value must be an integer number");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"maxlength\":\"abc\"}",
                "Invalid configuration for attribute pa: the maxlength value must be an integer number");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"maxlength\":10.0}",
                "Invalid configuration for attribute pa: the maxlength value must be an integer number");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT,
                "{\"default\":\"abc\", \"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\"}",
                "Invalid configuration for attribute pa: the default value does not match regex configuration");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT,
                "{\"default\":\"abc\", \"minlength\":5}",
                "Invalid configuration for attribute pa: the default value is smaller than minlength");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT,
                "{\"default\":\"abcabc\", \"maxlength\":5}",
                "Invalid configuration for attribute pa: the default value is greater than maxlength");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT,
                "{\"minlength\":50, \"maxlength\":10}",
                "Invalid configuration for attribute pa: the minlength is greater than maxlength");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT,
                "{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
                "Invalid configuration for attribute pa: the default value is smaller than minlength");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", TEXT, "{\"anyconf\":\"abc\"}",
                "Invalid configuration for attribute pa: the anyconf configuration attribute is unknown");
    }
}