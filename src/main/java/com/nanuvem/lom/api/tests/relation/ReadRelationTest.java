package com.nanuvem.lom.api.tests.relation;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Instance;
import com.nanuvem.lom.api.Relation;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;
import com.nanuvem.lom.api.tests.instance.InstanceHelper;

public abstract class ReadRelationTest extends LomTestCase {

    @Test
    public void findRelationById() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Instance source = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation = RelationHelper.createRelation(relationType, source, target);
        Relation foundRelation = facade.findRelationById(relation.getId());
        Assert.assertEquals(relation, foundRelation);
    }

    @Test
    public void listAllRelations() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Instance source = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation = RelationHelper.createRelation(relationType, source, target);
        List<Relation> allRelations = facade.listAllRelations();
        Assert.assertEquals(1, allRelations.size());
        Assert.assertEquals(relation, allRelations.get(0));
    }
}
