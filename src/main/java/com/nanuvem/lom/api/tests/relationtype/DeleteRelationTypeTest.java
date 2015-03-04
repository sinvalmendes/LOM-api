package com.nanuvem.lom.api.tests.relationtype;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;
import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;

public abstract class DeleteRelationTypeTest extends LomTestCase {

    @Test
    public void deleteRelationType() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Assert.assertEquals(1, facade.listAllRelationTypes().size());
        facade.deleteRelationType(relationType.getId());
        Assert.assertEquals(0, facade.listAllRelationTypes().size());
        Assert.assertNull(facade.findRelationTypeById(relationType.getId()));
    }
}
