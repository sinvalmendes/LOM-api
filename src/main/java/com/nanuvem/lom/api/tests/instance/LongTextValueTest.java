package com.nanuvem.lom.api.tests.instance;

import org.junit.Test;

import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.tests.LomTestCase;

import static com.nanuvem.lom.api.tests.instance.InstanceHelper.*;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.*;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.*;

public abstract class LongTextValueTest extends LomTestCase {
    @Test
    public void instanceWithValidValuesForTheConfigurationOfAttributesLongText() {
        createEntity("abc", "a");
        createOneAttribute("abc.a", null, "name", AttributeType.LONGTEXT, "{\"mandatory\": true}");
        createAndVerifyOneInstance("abc.a", "Jose");

        createEntity("abc", "b");
        createOneAttribute("abc.b", null, "name", AttributeType.LONGTEXT, "{\"default\" : \"default longtext\"}");
        createAndVerifyOneInstance("abc.b", (String) null);

        createEntity("abc", "c");
        createOneAttribute("abc.c", null, "name", AttributeType.LONGTEXT, "{\"default\" : \"default longtext\"}");
        createAndVerifyOneInstance("abc.c", "Jose");

        createEntity("abc", "d");
        createOneAttribute("abc.d", null, "name", AttributeType.LONGTEXT, "{\"minlength\" : 6}");
        createAndVerifyOneInstance("abc.d", "Johson");

        createEntity("abc", "e");
        createOneAttribute("abc.e", null, "name", AttributeType.LONGTEXT, "{\"mandatory\" : true, \"minlength\" : 6}");
        createAndVerifyOneInstance("abc.e", "Johson");

        createEntity("abc", "f");
        createOneAttribute("abc.f", null, "name", AttributeType.LONGTEXT,
                "{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
        createAndVerifyOneInstance("abc.f", (String) null);

        createEntity("abc", "g");
        createOneAttribute("abc.g", null, "name", AttributeType.LONGTEXT,
                "{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
        createAndVerifyOneInstance("abc.g", "abccab");

        createEntity("abc", "h");
        createOneAttribute("abc.h", null, "name", AttributeType.LONGTEXT, "{\"maxlength\" : 6}");
        createAndVerifyOneInstance("abc.h", (String) null);

        createEntity("abc", "i");
        createOneAttribute("abc.i", null, "name", AttributeType.LONGTEXT, "{\"mandatory\" : true, \"maxlength\" : 6}");
        createAndVerifyOneInstance("abc.i", "Johson");

        createEntity("abc", "j");
        createOneAttribute("abc.j", null, "name", AttributeType.LONGTEXT,
                "{\"mandatory\" : true, \"default\" : \"Johson\", \"maxlength\" : 6}");
        createAndVerifyOneInstance("abc.j", (String) null);

        createEntity("abc", "k");
        createOneAttribute("abc.k", null, "name", AttributeType.LONGTEXT,
                "{\"mandatory\" : true, \"default\" : \"Johson\", \"minlength\" : 6, \"maxlength\" : 6}");
        createAndVerifyOneInstance("abc.k", (String) null);

        createEntity("abc", "l");
        createOneAttribute("abc.l", null, "name", AttributeType.LONGTEXT,
                "{\"mandatory\" : true, \"default\" : \"Johson\", \"minlength\" : 6, \"maxlength\" : 6}");
        createAndVerifyOneInstance("abc.l", "abccba");
    }

    @Test
    public void instanceWithInvalidValuesForConfigurationOfLongTextAttributes() {
        createEntity("abc", "a");
        createEntity("abc", "b");
        createEntity("abc", "ba");
        createEntity("abc", "c");
        createEntity("abc", "ca");
        createEntity("abc", "d");
        createEntity("abc", "e");

        invalidValueForInstance("abc.a", null, "nameA", AttributeType.LONGTEXT, "{\"mandatory\" : true}", null,
                "Invalid value for the Instance. The value for the 'nameA' attribute is mandatory");
        invalidValueForInstance("abc.b", null, "nameB", AttributeType.LONGTEXT, "{\"minlength\" : 5}", "abcd",
                "Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters");
        invalidValueForInstance("abc.ba", null, "nameBA", AttributeType.LONGTEXT,
                "{\"mandatory\" : true, \"minlength\" : 5}", "",
                "Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, "
                        + "The value for 'nameBA' must have a minimum length of 5 characters");
        invalidValueForInstance("abc.c", null, "nameC", AttributeType.LONGTEXT, "{\"maxlength\" : 5}", "abcdef",
                "Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters");
        invalidValueForInstance("abc.ca", null, "nameCA", AttributeType.LONGTEXT,
                "{\"mandatory\" : true, \"maxlength\" : 5}", "",
                "Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory");
        invalidValueForInstance("abc.d", null, "nameD", AttributeType.LONGTEXT,
                "{\"minlength\" : 5, \"maxlength\" : 5}", "abcdef",
                "Invalid value for the Instance. The value for 'nameD' must have a maximum length of 5 characters");
        invalidValueForInstance("abc.e", null, "nameE", AttributeType.LONGTEXT,
                "{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}", "",
                "Invalid value for the Instance. The value for the 'nameE' attribute is mandatory, "
                        + "The value for 'nameE' must have a minimum length of 5 characters");
    }

}