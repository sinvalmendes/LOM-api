package com.nanuvem.lom.api.tests.attribute;

import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.*;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.createEntity;

import org.junit.Test;

import com.nanuvem.lom.api.Attribute;
import com.nanuvem.lom.api.tests.LomTestCase;

public abstract class LongTextAttributeTest extends LomTestCase {

    private static final String LONGTEXT_CONFIGURATION_PARTIAL = "{\"minlength\": 5,\"maxlength\": 15}";

    private static final String LONGTEXT_CONFIGURATION_COMPLETE = "{\"mandatory\": false, \"default\": \"long text\", "
            + "\"minlength\": 5,\"maxlength\": 15}";

    @Test
    public void validConfigurationForLongTextAttributeType() {
        createEntity("abc", "a");

        createAndVerifyOneAttribute("abc.a", 1, "p1", LONGTEXT, "{\"minlength\":10}");

        createAndVerifyOneAttribute("abc.a", 1, "p2", LONGTEXT, "{\"maxlength\":100000}");

        createAndVerifyOneAttribute("abc.a", 1, "p3", LONGTEXT,
                "{\"mandatory\":true, \"default\":\"long text\", \"minlength\":5, \"maxlength\":150000}");

        createAndVerifyOneAttribute("abc.a", 1, "p4", LONGTEXT, "");
    }

    @Test
    public void invalidConfigurationForLongTextAttributeType() {
        createEntity("abc", "a");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT, "{\"default\":10}",
                "Invalid configuration for attribute pa: the default value must be a string");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT, "{\"minlength\":\"abc\"}",
                "Invalid configuration for attribute pa: the minlength value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT, "{\"minlength\":10.0}",
                "Invalid configuration for attribute pa: the minlength value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT, "{\"maxlength\":\"abc\"}",
                "Invalid configuration for attribute pa: the maxlength value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT, "{\"maxlength\":10.0}",
                "Invalid configuration for attribute pa: the maxlength value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT, "{\"default\":\"abc\", \"minlength\":5}",
                "Invalid configuration for attribute pa: the default value is smaller than minlength");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT,
                "{\"default\":\"abcabc\", \"maxlength\":5}",
                "Invalid configuration for attribute pa: the default value is greater than maxlength");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT, "{\"minlength\":50, \"maxlength\":10}",
                "Invalid configuration for attribute pa: the minlength is greater than maxlength");

        expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", LONGTEXT,
                "{\"default\":\"abc\",\"minlength\":9, \"maxlength\":10}",
                "Invalid configuration for attribute pa: the default value is smaller than minlength");
    }

    @Test
    public void validChangeConfigurationForLongTextAttributeType() {
        createEntity("abc", "a");

        Attribute createdAttribute1 = createOneAttribute("abc.a", null, "pa", LONGTEXT, MANDATORY_TRUE);
        Attribute createdAttribute2 = createOneAttribute("abc.a", null, "pb", LONGTEXT, LONGTEXT_CONFIGURATION_COMPLETE);

        Attribute updatedAttribute11 = updateAttribute("abc.a", createdAttribute1, 1, "pa", LONGTEXT,
                LONGTEXT_CONFIGURATION_PARTIAL);
        verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);

        Attribute updatedAttribute12 = updateAttribute("abc.a", updatedAttribute11, 1, "pa", LONGTEXT,
                LONGTEXT_CONFIGURATION_COMPLETE);
        verifyUpdatedAttribute(updatedAttribute11, updatedAttribute12);

        Attribute updatedAttribute21 = updateAttribute("abc.a", createdAttribute2, 2, "pb", LONGTEXT,
                LONGTEXT_CONFIGURATION_PARTIAL);
        verifyUpdatedAttribute(createdAttribute2, updatedAttribute21);

        Attribute updatedAttribute22 = updateAttribute("abc.a", updatedAttribute21, 2, "pb", LONGTEXT, MANDATORY_TRUE);
        verifyUpdatedAttribute(updatedAttribute21, updatedAttribute22);

        Attribute updatedAttribute23 = updateAttribute("abc.a", updatedAttribute22, 2, "pb", LONGTEXT,
                "{\"default\":\"abc\"}");
        verifyUpdatedAttribute(updatedAttribute22, updatedAttribute23);

        Attribute updatedAttribute24 = updateAttribute("abc.a", updatedAttribute23, 2, "pb", LONGTEXT,
                "{\"default\":\"123\"}");
        verifyUpdatedAttribute(updatedAttribute23, updatedAttribute24);

    }

    @Test
    public void invalidChangeConfigurationForLongTextAttributeType() {
        createEntity("abc", "a");
        Attribute createdAttribute = createOneAttribute("abc.a", null, "pa", LONGTEXT, null);

        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT, "{\"mandatory\":10}",
                "Invalid configuration for attribute pa: the mandatory value must be true or false literals");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT,
                "{\"mandatory\":\"true\"}",
                "Invalid configuration for attribute pa: the mandatory value must be true or false literals");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT, "{\"default\":10}",
                "Invalid configuration for attribute pa: the default value must be a string");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT,
                "{\"minlength\":\"abc\"}",
                "Invalid configuration for attribute pa: the minlength value must be an integer number");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT, "{\"minlength\":10.0}",
                "Invalid configuration for attribute pa: the minlength value must be an integer number");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT,
                "{\"maxlength\":\"abc\"}",
                "Invalid configuration for attribute pa: the maxlength value must be an integer number");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT, "{\"maxlength\":10.0}",
                "Invalid configuration for attribute pa: the maxlength value must be an integer number");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT,
                "{\"default\":\"abc\", \"minlength\":5}",
                "Invalid configuration for attribute pa: the default value is smaller than minlength");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT,
                "{\"default\":\"abcabc\", \"maxlength\":5}",
                "Invalid configuration for attribute pa: the default value is greater than maxlength");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT,
                "{\"minlength\":50, \"maxlength\":10}",
                "Invalid configuration for attribute pa: the minlength is greater than maxlength");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT,
                "{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
                "Invalid configuration for attribute pa: the default value is smaller than minlength");
        expectExceptionOnUpdateInvalidAttribute("abc.a", createdAttribute, 1, "pa", LONGTEXT, "{\"anyconf\":\"abc\"}",
                "Invalid configuration for attribute pa: the anyconf configuration attribute is unknown");
    }
}