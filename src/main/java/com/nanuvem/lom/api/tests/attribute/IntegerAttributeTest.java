package com.nanuvem.lom.api.tests.attribute;

import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.createAndVerifyOneAttribute;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.expectExceptionOnCreateInvalidAttribute;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.createEntity;

import org.junit.Test;

import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.tests.LomTestCase;

public abstract class IntegerAttributeTest extends LomTestCase {

    @Test
    public void validConfigurationForIntegerAttributeType() {
        createEntity("abc", "a");

        createAndVerifyOneAttribute("abc.a", 1, "p1", AttributeType.INTEGER, "{\"minvalue\":-5}");

        createAndVerifyOneAttribute("abc.a", 1, "p2", AttributeType.INTEGER, "{\"maxvalue\":100000000000}");

        createAndVerifyOneAttribute("abc.a", 1, "p3", AttributeType.INTEGER,
                "{\"mandatory\":true, \"default\":10, \"minvalue\":5, \"maxvalue\":150000}");

        createAndVerifyOneAttribute("abc.a", 1, "p4", AttributeType.INTEGER, "");
    }

    @Test
    public void invalidConfigurationForIntegerAttributeType() {
        createEntity("abc", "a");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER, "{\"default\":\"abc\"}",
                "Invalid configuration for attribute pa: the default value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER, "{\"minvalue\":\"abc\"}",
                "Invalid configuration for attribute pa: the minvalue value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER, "{\"minvalue\":10.0}",
                "Invalid configuration for attribute pa: the minvalue value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER, "{\"maxvalue\":\"abc\"}",
                "Invalid configuration for attribute pa: the maxvalue value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER, "{\"maxvalue\":10.0}",
                "Invalid configuration for attribute pa: the maxvalue value must be an integer number");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER,
                "{\"default\":3, \"minvalue\":5}",
                "Invalid configuration for attribute pa: the default value is smaller than minvalue");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER,
                "{\"default\":12, \"maxvalue\":10}",
                "Invalid configuration for attribute pa: the default value is greater than maxvalue");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER,
                "{\"minvalue\":50, \"maxvalue\":10}",
                "Invalid configuration for attribute pa: the minvalue is greater than maxvalue");

        expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa", AttributeType.INTEGER,
                "{\"default\":7, \"minvalue\":9, \"maxvalue\":10}",
                "Invalid configuration for attribute pa: the default value is smaller than minvalue");
    }

}