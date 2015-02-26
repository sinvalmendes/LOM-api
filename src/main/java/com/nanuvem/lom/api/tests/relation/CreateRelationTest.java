package com.nanuvem.lom.api.tests.relation;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;
import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Instance;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.Relation;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;
import com.nanuvem.lom.api.tests.instance.InstanceHelper;

public abstract class CreateRelationTest extends LomTestCase {

    @Test
    public void createValidRelation() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Instance source = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target = InstanceHelper.createOneInstance(targetEntity, args);
        Relation relation = RelationHelper.createRelation(relationType, source, target);
        Assert.assertNotNull(relation);
        Assert.assertEquals(1, relation.getId().intValue());
        Assert.assertEquals(0, relation.getVersion().intValue());

    }

    @Test
    public void getsExceptionWhenRelationTypeIsNull(){
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        String[] args = new String[0];
        Instance source = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target = InstanceHelper.createOneInstance(targetEntity, args);
        try {
            RelationHelper.createRelation(null, source, target);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The relation type is mandatory!", me.getMessage());
        }
    }
    
    @Test
    public void getsExceptionWhenTriesToCreateARelationWithoutSourceInstance() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Instance target = InstanceHelper.createOneInstance(targetEntity, args);
        try {
            RelationHelper.createRelation(relationType, null, target);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The source instance is mandatory!", me.getMessage());
        }
    }

    @Test
    public void getsExceptionWhenTriesToCreateARelationWithoutTargetInstance() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Instance source = InstanceHelper.createOneInstance(sourceEntity, args);
        try {
            RelationHelper.createRelation(relationType, source, null);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The target instance is mandatory!", me.getMessage());
        }
    }

    @Test
    public void getsExceptionWhenTriesToCreateARelationWithAnUnexistentSourceInstance() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Instance source = new Instance();
        Instance target = InstanceHelper.createOneInstance(targetEntity, args);
        try {
            RelationHelper.createRelation(relationType, source, target);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The source instance is mandatory!", me.getMessage());
        }
    }

    @Test
    public void getsExceptionWhenTriesToCreateARelationWithAnUnexistentTargetInstance() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Instance source = InstanceHelper.createOneInstance(targetEntity, args);
        Instance target = new Instance();
        try {
            RelationHelper.createRelation(relationType, source, target);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The target instance is mandatory!", me.getMessage());
        }
    }
}
