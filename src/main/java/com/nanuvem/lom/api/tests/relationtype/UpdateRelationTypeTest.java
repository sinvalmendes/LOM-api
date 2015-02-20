package com.nanuvem.lom.api.tests.relationtype;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;
import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;

public abstract class UpdateRelationTypeTest extends LomTestCase {

	/*
	 * update source cardinality (1-1 -> *-1, *-1 -> 1-1) 
	 * update target cardinality (1-1 -> 1-*, 1-* -> 1-1) 1-1 -> *-* *-* -> 1-1
	 */

	@Test
	public void changeSourceType() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, false, null);
		Entity anotherSourceEntity = EntityHelper.createAndSaveOneEntity(
				"namespace", "AnotherSourceEntity");
		relationType.setSourceEntity(anotherSourceEntity);
		RelationType relationTypeUpdated = facade.update(relationType);
		Assert.assertEquals(relationTypeUpdated.getSourceEntity(),
				anotherSourceEntity);
		Assert.assertEquals(1, relationTypeUpdated.getVersion().intValue());
	}

	@Test
	public void getsExceptionWhenUpdateARelationTypeWithANonExistentSourceEntity() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, false, null);
		Entity anotherSourceEntity = new Entity();
		relationType.setSourceEntity(anotherSourceEntity);
		try {
			facade.update(relationType);
			Assert.fail();
		} catch (MetadataException me) {
			Assert.assertEquals(
					"Invalid argument: The source entity is mandatory!",
					me.getMessage());
		}
	}

	@Test
	public void changeTargetType() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, false, null);
		Entity anotherTargetEntity = new Entity();
		relationType.setTargetEntity(anotherTargetEntity);
		try {
			facade.update(relationType);
			Assert.fail();
		} catch (MetadataException me) {
			Assert.assertEquals(
					"Invalid argument: The target entity is mandatory!",
					me.getMessage());
		}
	}

	@Test
	public void updateForAValidReverseName() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, true, "ReverseName");
		String newReverseName = "NewReverseName";
		relationType.setReverseName(newReverseName);
		relationType = facade.update(relationType);
		Assert.assertEquals(1, relationType.getVersion().intValue());
		Assert.assertEquals(newReverseName, relationType.getReverseName());
	}

	@Test
	public void getsExceptionWhenTriesToUpdateReverseNameWithNullValue() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, true, "ReverseName");
		relationType.setReverseName(null);
		try {
			facade.update(relationType);
			Assert.fail();
		} catch (MetadataException me) {
			Assert.assertEquals(
					"Invalid argument: Reverse Name is mandatory when the relationship is bidirectional!",
					me.getMessage());
		}
	}

	@Test
	public void changeIsBidirectionalFromTrueToFalseShouldChangeReverseNameToNull() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, true, "ReverseName");
		relationType.setBidirectional(false);
		relationType = facade.update(relationType);
		Assert.assertEquals(1, relationType.getVersion().intValue());
		Assert.assertNull(relationType.getReverseName());
	}

	@Test
	public void getsExceptionWhenTriesToChangeIsBidirectionalFromFalseToTrueWithoutReverseName() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, false, null);
		relationType.setBidirectional(true);
		try {
			facade.update(relationType);
			Assert.fail();
		} catch (MetadataException me) {
			Assert.assertEquals(
					"Invalid argument: Reverse Name is mandatory when the relationship is bidirectional!",
					me.getMessage());
		}
	}

//	This test make sense?	
//	@Test
//	public void getExceptionWhenTriesToPutAReverseNameInANonBidirectionalRelationType() {
//		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
//				"SourceEntity");
//		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
//				"TargetEntity");
//		RelationType relationType = createRelationType("RelationType",
//				sourceEntity, targetEntity, null, null, false, null);
//		relationType.setReverseName("ReverseName");
//		try {
//			facade.update(relationType);
//			Assert.fail();
//		} catch (MetadataException me) {
//			Assert.assertEquals(
//					"Invalid argument: Reverse Name should be null when the relationship is not bidirectional!",
//					me.getMessage());
//		}
//	}
	
	
}
