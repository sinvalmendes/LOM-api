package com.nanuvem.lom.api.tests.relationtype;

import com.nanuvem.lom.api.Cardinality;
import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.Facade;
import com.nanuvem.lom.api.RelationType;

public class RelationTypeHelper {

    private static Facade facade;

    public static void setFacade(Facade facade) {
        RelationTypeHelper.facade = facade;
    }

    public static RelationType createRelationType(String name, EntityType sourceEntity, EntityType targetEntity,
            Cardinality sourceCardinality, Cardinality targetCardinality, boolean isBidirectional, String reverseName) {
        RelationType relationType = new RelationType();
        relationType.setName(name);
        relationType.setSourceEntityType(sourceEntity);
        relationType.setTargetEntityType(targetEntity);
        relationType.setSourceCardinality(sourceCardinality);
        relationType.setTargetCardinality(targetCardinality);
        relationType.setBidirectional(isBidirectional);
        relationType.setReverseName(reverseName);
        relationType = facade.create(relationType);
        return relationType;
    }

}
