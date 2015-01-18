package com.nanuvem.lom.api.tests.relationtype;

import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.Cardinality;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;

public abstract class CreateRelationType extends LomTestCase {

	@Test
	public void createRelationTypeWithStringName() {
		RelationType relationType = createRelationType("RelationType",
				new Entity(), new Entity(), null, null, false);
		Assert.assertEquals("RelationType",
				facade.findRelationTypeById(relationType.getId()).getName());
		Assert.assertEquals(1, facade.findAllRelationTypes().size());
	}

	@Test
	public void getsExceptionWhenCreateAnRelationTypeWithoutName() {
		try {
			createRelationType(null, new Entity(), new Entity(), null, null,
					false);
		} catch (MetadataException me) {
			Assert.assertEquals("Name is mandatory!", me.getMessage());
		}
	}

	@Test
	public void createRelationTypeWithValidSourceAndTargetEntity() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, false);
		Assert.assertEquals(sourceEntity, relationType.getSourceEntity());
		Assert.assertEquals(targetEntity, relationType.getTargetEntity());
	}

	@Test
	public void getsExceptionWhenCreateAnRelationTypeWithouSourceEntity() {
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		try {
			createRelationType("RelationType", null, targetEntity, null, null,
					false);
		} catch (MetadataException me) {
			Assert.assertEquals("SourceEntity is mandatory!", me.getMessage());
		}
	}

	@Test
	public void getsExceptionWhenCreateAnRelationTypeWithouTargetEntity() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		try {
			createRelationType("RelationType", sourceEntity, null, null, null,
					false);
		} catch (MetadataException me) {
			Assert.assertEquals("TargetEntity is mandatory!", me.getMessage());
		}
	}

	@Test
	public void sourceCardinalityDefaultValueIsONE() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, false);
		Assert.assertEquals(relationType.getSourceCardinality(),
				Cardinality.ONE);
	}

	@Test
	public void targetCardinalityDefaultValueIsONE() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, false);
		Assert.assertEquals(relationType.getTargetCardinality(),
				Cardinality.ONE);
	}

	@Test
	public void bidirectionalDefaultValueIsFalse() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = new RelationType();
		relationType.setSourceEntity(sourceEntity);
		relationType.setTargetEntity(targetEntity);
		Assert.assertFalse(relationType.isBidirectional());
	}

	@Test
	public void reverseNameShouldBeNullWhenBidirectionalIsFalse(){
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, false);
		Assert.assertNull(relationType.getReverseName());
	}
	
	@Test
	public void getsExceptionBecauseTheReverseNameIsMandatoryIfBidirectionalIsTrue() {
		Entity sourceEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"SourceEntity");
		Entity targetEntity = EntityHelper.createAndSaveOneEntity("namespace",
				"TargetEntity");
		RelationType relationType = createRelationType("RelationType",
				sourceEntity, targetEntity, null, null, true);
		try {
			facade.create(relationType);
		} catch (MetadataException me) {
			Assert.assertEquals(
					"ReverseName is mandatory when the relationship is bidirectional!",
					me.getMessage());
		}
	}
}
