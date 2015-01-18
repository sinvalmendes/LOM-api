package com.nanuvem.lom.api.tests.relationtype;

import com.nanuvem.lom.api.Cardinality;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.RelationType;

public class RelationTypeHelper {

	private static Facade facade;

	public static RelationType createRelationType(String name,
			Entity sourceEntity, Entity targetEntity,
			Cardinality sourceCardinality, Cardinality targetCardinality, boolean isBidirectional) {
		RelationType relationType = new RelationType();
		relationType.setName(name);
		relationType.setSourceEntity(sourceEntity);
		relationType.setTargetEntity(targetEntity);
		relationType.setSourceCardinality(sourceCardinality);
		relationType.setTargetCardinality(targetCardinality);
		relationType.setBidirectional(isBidirectional);
		relationType = facade.create(relationType);
		return relationType;
	}
}
