package com.nanuvem.lom.api.tests.relationtype;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;
import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;

public abstract class ReadRelationTypeTest extends LomTestCase {

    @Test
    public void listAllRelationTypes() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        Assert.assertEquals(0, facade.listAllRelationTypes().size());
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Assert.assertEquals(1, facade.listAllRelationTypes().size());
        Assert.assertEquals(relationType, facade.listAllRelationTypes().get(0));

    }

    @Test
    public void findRelationTypeById() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        RelationType foundRelationType = facade.findRelationTypeById(relationType.getId());
        Assert.assertEquals(relationType, foundRelationType);
    }

}
