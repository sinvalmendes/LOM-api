package com.nanuvem.lom.api.tests.instance;

import org.junit.Test;

import com.nanuvem.lom.api.AttributeType;
import com.nanuvem.lom.api.tests.LomTestCase;

import static com.nanuvem.lom.api.tests.instance.InstanceHelper.*;
import static com.nanuvem.lom.api.tests.attribute.AttributeHelper.*;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.*;

public abstract class PasswordValueTest extends LomTestCase {

    @Test
    public void instanceWithValidValuesForTheConfigurationOfAttributesPassword() {
        createEntity("abc", "a");
        createOneAttribute("abc.a", null, "secretKey", AttributeType.PASSWORD, "{\"default\": \"password\"}");
        createAndVerifyOneInstance("abc.a", (String) null);

        createEntity("abc", "b");
        createOneAttribute("abc.b", null, "secretKey", AttributeType.PASSWORD, "{\"minUppers\": 2}");
        createAndVerifyOneInstance("abc.b", "SecretKey");

        createEntity("abc", "c");
        createOneAttribute("abc.c", null, "secretKey", AttributeType.PASSWORD, "{\"minNumbers\": 2}");
        createAndVerifyOneInstance("abc.c", "1secretkey2");

        createEntity("abc", "d");
        createOneAttribute("abc.d", null, "secretKey", AttributeType.PASSWORD, "{\"minSymbols\": 2}");
        createAndVerifyOneInstance("abc.d", "&secretkey%");

        createEntity("abc", "e");
        createOneAttribute("abc.e", null, "secretKey", AttributeType.PASSWORD, "{\"maxRepeat\": 2}");
        createAndVerifyOneInstance("abc.e", "secretkey");

        createEntity("abc", "f");
        createOneAttribute("abc.f", null, "secretKey", AttributeType.PASSWORD, "{\"minlength\": 6}");
        createAndVerifyOneInstance("abc.f", "secret");

        createEntity("abc", "g");
        createOneAttribute("abc.g", null, "secretKey", AttributeType.PASSWORD, "{\"maxlength\": 6}");
        createAndVerifyOneInstance("abc.g", "secret");

        createEntity("abc", "h");
        createOneAttribute("abc.h", null, "secretKey", AttributeType.PASSWORD, "{\"mandatory\": true}");
        createAndVerifyOneInstance("abc.h", "secret");

        createEntity("abc", "i");
        createOneAttribute("abc.i", null, "secretKey", AttributeType.PASSWORD, "{\"mandatory\": false}");
        createAndVerifyOneInstance("abc.i", (String) null);

        createEntity("abc", "j");
        createOneAttribute("abc.j", null, "secretKey", AttributeType.PASSWORD,
                "{\"mandatory\": true, \"default\": \"mypassword\"}");
        createAndVerifyOneInstance("abc.j", (String) null);

        createEntity("abc", "k");
        createOneAttribute("abc.k", null, "secretKey", AttributeType.PASSWORD,
                "{\"mandatory\": true, \"default\": \"P@ss%W04\", \"minlength\": 6, "
                        + "\"maxlength\": 9, \"minUppers\": 2, \"minNumbers\": 2, "
                        + "\"minSymbols\": 2, \"maxRepeat\": 2}");
        createAndVerifyOneInstance("abc.k", "0tH3r@$Ss");
    }

    @Test
    public void instanceWithInvalidValuesForTheConfigurationOfAttributesPassword() {
        createEntity("abc", "a");
        createEntity("abc", "b");
        createEntity("abc", "ba");
        createEntity("abc", "c");
        createEntity("abc", "ca");
        createEntity("abc", "d");
        createEntity("abc", "da");
        createEntity("abc", "e");
        createEntity("abc", "f");
        createEntity("abc", "g");
        createEntity("abc", "h");
        createEntity("abc", "i");
        createEntity("abc", "j");

        invalidValueForInstance("abc.a", null, "nameA", AttributeType.PASSWORD, "{\"mandatory\" : true}", null,
                "Invalid value for the Instance. The value for the 'nameA' attribute is mandatory");
        invalidValueForInstance("abc.b", null, "nameB", AttributeType.PASSWORD, "{\"minlength\" : 5}", "abcd",
                "Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters");
        invalidValueForInstance("abc.ba", null, "nameBA", AttributeType.PASSWORD,
                "{\"mandatory\" : true, \"minlength\" : 5}", "",
                "Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, "
                        + "The value for 'nameBA' must have a minimum length of 5 characters");
        invalidValueForInstance("abc.c", null, "nameC", AttributeType.PASSWORD, "{\"maxlength\" : 5}", "abcdef",
                "Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters");
        invalidValueForInstance("abc.ca", null, "nameCA", AttributeType.PASSWORD,
                "{\"mandatory\" : true, \"maxlength\" : 5}", "",
                "Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory");
        invalidValueForInstance("abc.d", null, "nameD", AttributeType.PASSWORD,
                "{\"minlength\" : 5, \"maxlength\" : 5}", "abcdef",
                "Invalid value for the Instance. The value for 'nameD' must have a maximum length of 5 characters");
        invalidValueForInstance("abc.da", null, "nameDA", AttributeType.PASSWORD,
                "{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}", "",
                "Invalid value for the Instance. The value for the 'nameDA' attribute is mandatory, "
                        + "The value for 'nameDA' must have a minimum length of 5 characters");
        invalidValueForInstance("abc.e", null, "nameE", AttributeType.PASSWORD, "{\"minUppers\" : 3}", "ABcdef",
                "Invalid value for the Instance. The value for 'nameE' must have at least 3 uppercase characters");
        invalidValueForInstance("abc.f", null, "nameF", AttributeType.PASSWORD, "{\"minNumbers\" : 3}", "abc12def",
                "Invalid value for the Instance. The value for 'nameF' must be at least 3 numbers");
        invalidValueForInstance("abc.g", null, "nameG", AttributeType.PASSWORD, "{\"minSymbols\" : 3}", "ab%c12def*",
                "Invalid value for the Instance. The value for 'nameG' must have at least 3 symbol characters");
        invalidValueForInstance("abc.h", null, "nameH", AttributeType.PASSWORD, "{\"maxRepeat\" : 0}", "ab%c1a2e*",
                "Invalid value for the Instance. The value for 'nameH' must not have repeated characters");
        invalidValueForInstance("abc.i", null, "nameI", AttributeType.PASSWORD, "{\"maxRepeat\" : 1}", "ab%ac1a2e*",
                "Invalid value for the Instance. The value for 'nameI' must not have more than 2 repeated characters");
        invalidValueForInstance("abc.j", null, "nameJ", AttributeType.PASSWORD,
                "{\"mandatory\": true, \"minlength\": 4, \"maxlength\": 4, \"minUppers\" : 3, \"minNumbers\" : 2, "
                        + "\"minSymbols\" : 1, \"maxRepeat\" : 0}", "ab1CfdeFa",
                "Invalid value for the Instance. The value for 'nameJ' must have a maximum length of 4 characters, "
                        + "The value for 'nameJ' must have at least 3 uppercase characters, "
                        + "The value for 'nameJ' must be at least 2 numbers, "
                        + "The value for 'nameJ' must have at least 1 symbol character, "
                        + "The value for 'nameJ' must not have repeated characters");
    }
}