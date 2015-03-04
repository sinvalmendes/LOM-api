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

public abstract class CreateRelationTest extends LomTestCase {

    @Test
    public void createValidRelation() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Entity source = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target = InstanceHelper.createOneInstance(targetEntity, args);
        Relation relation = RelationHelper.createRelation(relationType, source, target);
        Assert.assertNotNull(relation);
        Assert.assertEquals(1, relation.getId().intValue());
        Assert.assertEquals(0, relation.getVersion().intValue());

    }

    @Test
    public void getsExceptionWhenRelationTypeIsNull(){
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        String[] args = new String[0];
        Entity source = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target = InstanceHelper.createOneInstance(targetEntity, args);
        try {
            RelationHelper.createRelation(null, source, target);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The relation type is mandatory!", me.getMessage());
        }
    }
    
    @Test
    public void getsExceptionWhenTriesToCreateARelationWithoutSourceInstance() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Entity target = InstanceHelper.createOneInstance(targetEntity, args);
        try {
            RelationHelper.createRelation(relationType, null, target);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The source instance is mandatory!", me.getMessage());
        }
    }

    @Test
    public void getsExceptionWhenTriesToCreateARelationWithoutTargetInstance() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Entity source = InstanceHelper.createOneInstance(sourceEntity, args);
        try {
            RelationHelper.createRelation(relationType, source, null);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The target instance is mandatory!", me.getMessage());
        }
    }

    @Test
    public void getsExceptionWhenTriesToCreateARelationWithAnUnexistentSourceInstance() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Entity source = new Entity();
        Entity target = InstanceHelper.createOneInstance(targetEntity, args);
        try {
            RelationHelper.createRelation(relationType, source, target);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The source instance is mandatory!", me.getMessage());
        }
    }

    @Test
    public void getsExceptionWhenTriesToCreateARelationWithAnUnexistentTargetInstance() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        String[] args = new String[0];
        Entity source = InstanceHelper.createOneInstance(targetEntity, args);
        Entity target = new Entity();
        try {
            RelationHelper.createRelation(relationType, source, target);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The target instance is mandatory!", me.getMessage());
        }
    }
}
