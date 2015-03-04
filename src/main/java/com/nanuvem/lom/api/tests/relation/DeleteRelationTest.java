package com.nanuvem.lom.api.tests.relation;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;
import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.Relation;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;
import com.nanuvem.lom.api.tests.instance.InstanceHelper;

public abstract class DeleteRelationTest extends LomTestCase {

    @Test
    public void deleteRelation() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Entity source = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target = InstanceHelper.createOneInstance(targetEntity, args);
        Relation relation = RelationHelper.createRelation(relationType, source, target);
        facade.deleteRelation(relation.getId());
        Assert.assertEquals(0, facade.listAllRelations().size());
        try {
            facade.deleteRelation(relation.getId() + 10);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Unknown id for Relation: 11", me.getMessage());
        }

    }
}
