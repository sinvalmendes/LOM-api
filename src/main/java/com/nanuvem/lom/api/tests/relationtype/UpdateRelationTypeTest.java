package com.nanuvem.lom.api.tests.relationtype;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.Cardinality;
import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.Relation;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;
import com.nanuvem.lom.api.tests.instance.InstanceHelper;
import com.nanuvem.lom.api.tests.relation.RelationHelper;

public abstract class UpdateRelationTypeTest extends LomTestCase {

    // update source cardinality (1-1 -> *-1, *-1 -> 1-1)
    // update target cardinality (1-1 -> 1-*, 1-* -> 1-1)
    // 1-1 -> *-* *-* -> 1-1

    @Test
    public void changeSourceType() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        EntityType anotherSourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "AnotherSourceEntity");
        relationType.setSourceEntityType(anotherSourceEntity);
        RelationType relationTypeUpdated = facade.update(relationType);
        Assert.assertEquals(relationTypeUpdated.getSourceEntityType(), anotherSourceEntity);
        Assert.assertEquals(1, relationTypeUpdated.getVersion().intValue());
    }

    @Test
    public void getsExceptionWhenUpdateARelationTypeWithANonExistentSourceEntity() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        EntityType anotherSourceEntity = new EntityType();
        relationType.setSourceEntityType(anotherSourceEntity);
        try {
            facade.update(relationType);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The source entity is mandatory!", me.getMessage());
        }
    }

    @Test
    public void changeTargetType() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        EntityType anotherTargetEntity = new EntityType();
        relationType.setTargetEntityType(anotherTargetEntity);
        try {
            facade.update(relationType);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The target entity is mandatory!", me.getMessage());
        }
    }

    @Test
    public void updateForAValidReverseName() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, true,
                "ReverseName");
        String newReverseName = "NewReverseName";
        relationType.setReverseName(newReverseName);
        relationType = facade.update(relationType);
        Assert.assertEquals(1, relationType.getVersion().intValue());
        Assert.assertEquals(newReverseName, relationType.getReverseName());
    }

    @Test
    public void getsExceptionWhenTriesToUpdateReverseNameWithNullValue() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, true,
                "ReverseName");
        relationType.setReverseName(null);
        try {
            facade.update(relationType);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: Reverse Name is mandatory when the relationship is bidirectional!",
                    me.getMessage());
        }
    }

    @Test
    public void changeIsBidirectionalFromTrueToFalseShouldChangeReverseNameToNull() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, true,
                "ReverseName");
        relationType.setBidirectional(false);
        relationType = facade.update(relationType);
        Assert.assertEquals(1, relationType.getVersion().intValue());
        Assert.assertNull(relationType.getReverseName());
    }

    @Test
    public void getsExceptionWhenTriesToChangeIsBidirectionalFromFalseToTrueWithoutReverseName() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        relationType.setBidirectional(true);
        try {
            facade.update(relationType);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: Reverse Name is mandatory when the relationship is bidirectional!",
                    me.getMessage());
        }
    }

    /*
     * @Test public void
     * getExceptionWhenTriesToPutAReverseNameInANonBidirectionalRelationType() {
     * Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
     * "SourceEntity"); Entity targetEntity =
     * EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
     * RelationType relationType = createRelationType("RelationType",
     * sourceEntity, targetEntity, null, null, false, null);
     * relationType.setReverseName("ReverseName"); try {
     * facade.update(relationType); Assert.fail(); } catch (MetadataException
     * me) { Assert.assertEquals(
     * "Invalid argument: Reverse Name should be null when the relationship is not bidirectional!"
     * , me.getMessage()); } }
     */

    @Test
    public void updateAnOneToOneRelationTypeToOneToMany() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.ONE, false, null);
        String[] args = new String[0];
        Entity source = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation1 = RelationHelper.createRelation(relationType, source, target);
        Entity target2 = InstanceHelper.createOneInstance(targetEntity, args);

        try {
            RelationHelper.createRelation(relationType, source, target2);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals(
                    "Invalid argument, the target cardinality is ONE, the target instance cannot be associated to the source instance!",
                    me.getMessage());
        }
        relationType.setTargetCardinality(Cardinality.MANY);
        facade.update(relationType);
        Relation relation2 = RelationHelper.createRelation(relationType, source, target2);
        List<Relation> allRelations = facade.findRelationsBySourceInstance(source, relationType);

        Assert.assertEquals(relation1, allRelations.get(0));
        Assert.assertEquals(relation2, allRelations.get(1));
        Assert.assertEquals(target, allRelations.get(0).getTarget());
        Assert.assertEquals(target2, allRelations.get(1).getTarget());
    }

    @Test
    public void updateAnOneToManyRelationTypeToOneToOne() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.MANY, false, null);
        String[] args = new String[0];
        Entity source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target1 = InstanceHelper.createOneInstance(targetEntity, args);
        Entity target2 = InstanceHelper.createOneInstance(targetEntity, args);
        Entity target3 = InstanceHelper.createOneInstance(targetEntity, args);
        Entity target4 = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);
        Relation relation2 = RelationHelper.createRelation(relationType, source1, target2);
        Relation relation3 = RelationHelper.createRelation(relationType, source2, target3);
        Relation relation4 = RelationHelper.createRelation(relationType, source2, target4);

        relationType.setTargetCardinality(Cardinality.ONE);
        try {
            facade.update(relationType);
        } catch (MetadataException me) {
            Assert.assertEquals(
                    "Invalid update: there are more than one Relation with the actual cardinality configuration!",
                    me.getMessage());
        }
        List<Relation> allRelationsSource1 = facade.findRelationsBySourceInstance(source1, relationType);
        List<Relation> allRelationsSource2 = facade.findRelationsBySourceInstance(source2, relationType);
        Assert.assertEquals(2, allRelationsSource1.size());
        Assert.assertEquals(2, allRelationsSource2.size());
    }

    @Test
    public void updateAOneToManyRelationTypeToManyToMany() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.MANY, false, null);
        String[] args = new String[0];
        Entity source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target1 = InstanceHelper.createOneInstance(targetEntity, args);
        Entity target2 = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);
        Relation relation2 = RelationHelper.createRelation(relationType, source1, target2);

        try {
            RelationHelper.createRelation(relationType, source2, target2);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals(
                    "Invalid argument, the source cardinality is ONE, the target instance cannot be associated to the source instance!",
                    me.getMessage());
        }
        relationType.setSourceCardinality(Cardinality.MANY);
        facade.update(relationType);
        Relation relation3 = RelationHelper.createRelation(relationType, source2, target2);
        List<Relation> allRelationsSource1 = facade.findRelationsBySourceInstance(source1, relationType);
        List<Relation> allRelationsSource2 = facade.findRelationsBySourceInstance(source2, relationType);

        Assert.assertEquals(2, allRelationsSource1.size());
        Assert.assertEquals(relation1, allRelationsSource1.get(0));
        Assert.assertEquals(relation2, allRelationsSource1.get(1));
        Assert.assertEquals(1, allRelationsSource2.size());
        Assert.assertEquals(relation3, allRelationsSource2.get(0));
    }

    @Test
    public void updateAManyToManyRelationTypeToOneToMany() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.MANY,
                Cardinality.MANY, false, null);
        String[] args = new String[0];
        Entity source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target1 = InstanceHelper.createOneInstance(targetEntity, args);
        Entity target2 = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);
        Relation relation2 = RelationHelper.createRelation(relationType, source1, target2);
        Relation relation3 = RelationHelper.createRelation(relationType, source2, target1);
        Relation relation4 = RelationHelper.createRelation(relationType, source2, target2);

        relationType = facade.findRelationTypeById(relationType.getId());
        relationType.setSourceCardinality(Cardinality.ONE);
        try {
            facade.update(relationType);
        } catch (MetadataException me) {
            Assert.assertEquals(
                    "Invalid update: there are more than one Relation with the actual cardinality configuration!",
                    me.getMessage());
        }
        List<Relation> allRelationsSource1 = facade.findRelationsBySourceInstance(source1, relationType);
        List<Relation> allRelationsSource2 = facade.findRelationsBySourceInstance(source2, relationType);
        Assert.assertEquals(2, allRelationsSource1.size());
        Assert.assertEquals(2, allRelationsSource2.size());
    }

    @Test
    public void updateAnOneToOneRelationTypeToManyToMany() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.ONE, false, null);
        String[] args = new String[0];
        Entity source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target1 = InstanceHelper.createOneInstance(targetEntity, args);
        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);

        Entity source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target2 = InstanceHelper.createOneInstance(targetEntity, args);

        try {
            RelationHelper.createRelation(relationType, source1, target2);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals(
                    "Invalid argument, the target cardinality is ONE, the target instance cannot be associated to the source instance!",
                    me.getMessage());
        }
        relationType.setSourceCardinality(Cardinality.MANY);
        relationType.setTargetCardinality(Cardinality.MANY);
        facade.update(relationType);

        Relation relation2 = RelationHelper.createRelation(relationType, source1, target2);
        Relation relation3 = RelationHelper.createRelation(relationType, source2, target2);

        List<Relation> relationsOfSource1 = facade.findRelationsBySourceInstance(source1, relationType);
        List<Relation> relationsOfSource2 = facade.findRelationsBySourceInstance(source2, relationType);

        Assert.assertEquals(relationsOfSource1.get(0), relation1);
        Assert.assertEquals(relationsOfSource1.get(1), relation2);
        Assert.assertEquals(relationsOfSource2.get(0), relation3);
        Assert.assertEquals(relationsOfSource1.get(0).getTarget(), target1);
        Assert.assertEquals(relationsOfSource1.get(1).getTarget(), target2);
        Assert.assertEquals(relationsOfSource2.get(0).getTarget(), target2);
    }

    @Test
    public void updateAManyToManyRelationTypeToOneToOne() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.MANY,
                Cardinality.MANY, false, null);
        String[] args = new String[0];
        Entity source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target1 = InstanceHelper.createOneInstance(targetEntity, args);

        Entity source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Entity target2 = InstanceHelper.createOneInstance(targetEntity, args);
        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);
        Relation relation2 = RelationHelper.createRelation(relationType, source1, target2);
        Relation relation3 = RelationHelper.createRelation(relationType, source2, target1);
        Relation relation4 = RelationHelper.createRelation(relationType, source2, target2);

        relationType.setSourceCardinality(Cardinality.ONE);
        relationType.setTargetCardinality(Cardinality.ONE);
        try {
            facade.update(relationType);
        } catch (MetadataException me) {
            Assert.assertEquals(
                    "Invalid update: there are more than one Relation with the actual cardinality configuration!",
                    me.getMessage());
        }
        Assert.assertEquals(4, facade.findRelationsByRelationType(relationType).size());
        Assert.assertEquals(relation1, facade.findRelationsByRelationType(relationType).get(0));

        try {
            RelationHelper.createRelation(relationType, source1, target2);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals(
                    "Invalid argument, the target cardinality is ONE, the target instance cannot be associated to the source instance!",
                    me.getMessage());
        }
    }

}