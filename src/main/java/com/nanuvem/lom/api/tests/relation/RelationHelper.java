package com.nanuvem.lom.api.tests.relation;

import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.Instance;
import com.nanuvem.lom.api.Relation;
import com.nanuvem.lom.api.RelationType;

public class RelationHelper {

	private static Facade facade;

	public static void setFacade(Facade facade) {
		RelationHelper.facade = facade;
	}

	public static Relation createRelation(RelationType relationType, Instance source, Instance target) {
		Relation relation = new Relation();
		relation.setRelationType(relationType);
		relation.setSource(source);
		relation.setTarget(target);
		return facade.create(relation);
	}
}
