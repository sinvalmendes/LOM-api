package com.nanuvem.lom.api.tests.relationtype;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;
import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;

public abstract class ReadRelationTypeTest extends LomTestCase {

    @Test
    public void listAllRelationTypes() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        Assert.assertEquals(0, facade.listAllRelationTypes().size());
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Assert.assertEquals(1, facade.listAllRelationTypes().size());
        Assert.assertEquals(relationType, facade.listAllRelationTypes().get(0));

    }

    @Test
    public void findRelationTypeById() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        RelationType foundRelationType = facade.findRelationTypeById(relationType.getId());
        Assert.assertEquals(relationType, foundRelationType);
    }

}
