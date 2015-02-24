package com.nanuvem.lom.api.tests.relationtype;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.Cardinality;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Instance;
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
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Entity anotherSourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "AnotherSourceEntity");
        relationType.setSourceEntity(anotherSourceEntity);
        RelationType relationTypeUpdated = facade.update(relationType);
        Assert.assertEquals(relationTypeUpdated.getSourceEntity(), anotherSourceEntity);
        Assert.assertEquals(1, relationTypeUpdated.getVersion().intValue());
    }

    @Test
    public void getsExceptionWhenUpdateARelationTypeWithANonExistentSourceEntity() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Entity anotherSourceEntity = new Entity();
        relationType.setSourceEntity(anotherSourceEntity);
        try {
            facade.update(relationType);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The source entity is mandatory!", me.getMessage());
        }
    }

    @Test
    public void changeTargetType() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Entity anotherTargetEntity = new Entity();
        relationType.setTargetEntity(anotherTargetEntity);
        try {
            facade.update(relationType);
            Assert.fail();
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid argument: The target entity is mandatory!", me.getMessage());
        }
    }

    @Test
    public void updateForAValidReverseName() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
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
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
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
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, true,
                "ReverseName");
        relationType.setBidirectional(false);
        relationType = facade.update(relationType);
        Assert.assertEquals(1, relationType.getVersion().intValue());
        Assert.assertNull(relationType.getReverseName());
    }

    @Test
    public void getsExceptionWhenTriesToChangeIsBidirectionalFromFalseToTrueWithoutReverseName() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
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

    // This test make sense?
    // @Test
    // public void
    // getExceptionWhenTriesToPutAReverseNameInANonBidirectionalRelationType() {
    // Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
    // "SourceEntity");
    // Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
    // "TargetEntity");
    // RelationType relationType = createRelationType("RelationType",
    // sourceEntity, targetEntity, null, null, false, null);
    // relationType.setReverseName("ReverseName");
    // try {
    // facade.update(relationType);
    // Assert.fail();
    // } catch (MetadataException me) {
    // Assert.assertEquals(
    // "Invalid argument: Reverse Name should be null when the relationship is not bidirectional!",
    // me.getMessage());
    // }
    // }

    @Test
    public void updateAnOneToOneRelationTypeToOneToMany() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.ONE, false, null);
        String[] args = new String[0];
        Instance source = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation1 = RelationHelper.createRelation(relationType, source, target);
        Instance target2 = InstanceHelper.createOneInstance(targetEntity, args);

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
        List<Relation> allRelations = facade.findRelationsBySourceInstance(source);

        Assert.assertEquals(relation1, allRelations.get(0));
        Assert.assertEquals(relation2, allRelations.get(1));
        Assert.assertEquals(target, allRelations.get(0).getTarget());
        Assert.assertEquals(target2, allRelations.get(1).getTarget());
    }

    @Test
    public void updateAnOneToManyRelationTypeToOneToOne() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.MANY, false, null);
        String[] args = new String[0];
        Instance source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target1 = InstanceHelper.createOneInstance(targetEntity, args);
        Instance target2 = InstanceHelper.createOneInstance(targetEntity, args);
        Instance target3 = InstanceHelper.createOneInstance(targetEntity, args);
        Instance target4 = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);
        Relation relation2 = RelationHelper.createRelation(relationType, source1, target2);
        Relation relation3 = RelationHelper.createRelation(relationType, source2, target3);
        Relation relation4 = RelationHelper.createRelation(relationType, source2, target4);

        relationType.setTargetCardinality(Cardinality.ONE);
        facade.update(relationType);

        List<Relation> allRelationsSource1 = facade.findRelationsBySourceInstance(source1);
        List<Relation> allRelationsSource2 = facade.findRelationsBySourceInstance(source2);

        Assert.assertEquals(1, allRelationsSource1.size());
        Assert.assertEquals(relation1, allRelationsSource1.get(0));
        Assert.assertEquals(1, allRelationsSource2.size());
        Assert.assertEquals(relation3, allRelationsSource2.get(0));
    }

    @Test
    public void updateAOneToManyRelationTypeToManyToMany() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.MANY, false, null);
        String[] args = new String[0];
        Instance source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target1 = InstanceHelper.createOneInstance(targetEntity, args);
        Instance target2 = InstanceHelper.createOneInstance(targetEntity, args);

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
        List<Relation> allRelationsSource1 = facade.findRelationsBySourceInstance(source1);
        List<Relation> allRelationsSource2 = facade.findRelationsBySourceInstance(source2);

        Assert.assertEquals(2, allRelationsSource1.size());
        Assert.assertEquals(relation1, allRelationsSource1.get(0));
        Assert.assertEquals(relation2, allRelationsSource1.get(1));
        Assert.assertEquals(1, allRelationsSource2.size());
        Assert.assertEquals(relation3, allRelationsSource2.get(0));
    }

    @Test
    public void updateAManyToManyRelationTypeToOneToMany() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.MANY,
                Cardinality.MANY, false, null);
        String[] args = new String[0];
        Instance source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target1 = InstanceHelper.createOneInstance(targetEntity, args);
        Instance target2 = InstanceHelper.createOneInstance(targetEntity, args);

        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);
        Relation relation2 = RelationHelper.createRelation(relationType, source1, target2);
        Relation relation3 = RelationHelper.createRelation(relationType, source2, target1);
        Relation relation4 = RelationHelper.createRelation(relationType, source2, target2);

        relationType = facade.findRelationTypeById(relationType.getId());
        relationType.setSourceCardinality(Cardinality.ONE);
        facade.update(relationType);

        List<Relation> allRelationsSource1 = facade.findRelationsBySourceInstance(source1);
        List<Relation> allRelationsSource2 = facade.findRelationsBySourceInstance(source2);

        Assert.assertEquals(2, allRelationsSource1.size());
        Assert.assertEquals(relation1, allRelationsSource1.get(0));
        Assert.assertEquals(relation2, allRelationsSource1.get(1));
        Assert.assertEquals(0, allRelationsSource2.size());
    }

    @Test
    public void updateAnOneToOneRelationTypeToManyToMany() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.ONE, false, null);
        String[] args = new String[0];
        Instance source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target1 = InstanceHelper.createOneInstance(targetEntity, args);
        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);

        Instance source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target2 = InstanceHelper.createOneInstance(targetEntity, args);

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

        List<Relation> relationsOfSource1 = facade.findRelationsBySourceInstance(source1);
        List<Relation> relationsOfSource2 = facade.findRelationsBySourceInstance(source2);

        Assert.assertEquals(relationsOfSource1.get(0), relation1);
        Assert.assertEquals(relationsOfSource1.get(1), relation2);
        Assert.assertEquals(relationsOfSource2.get(0), relation3);
        Assert.assertEquals(relationsOfSource1.get(0).getTarget(), target1);
        Assert.assertEquals(relationsOfSource1.get(1).getTarget(), target2);
        Assert.assertEquals(relationsOfSource2.get(0).getTarget(), target2);
    }

    @Test
    public void updateAManyToManyRelationTypeToOneToOne() {
        Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.MANY,
                Cardinality.MANY, false, null);
        String[] args = new String[0];
        Instance source1 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target1 = InstanceHelper.createOneInstance(targetEntity, args);

        Instance source2 = InstanceHelper.createOneInstance(sourceEntity, args);
        Instance target2 = InstanceHelper.createOneInstance(targetEntity, args);
        Relation relation1 = RelationHelper.createRelation(relationType, source1, target1);
        Relation relation2 = RelationHelper.createRelation(relationType, source1, target2);
        Relation relation3 = RelationHelper.createRelation(relationType, source2, target1);
        Relation relation4 = RelationHelper.createRelation(relationType, source2, target2);

        relationType.setSourceCardinality(Cardinality.ONE);
        relationType.setTargetCardinality(Cardinality.ONE);
        facade.update(relationType);

        Assert.assertEquals(1, facade.findRelationsByRelationType(relationType).size());
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