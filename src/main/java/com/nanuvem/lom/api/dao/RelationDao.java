package com.nanuvem.lom.api.dao;

import java.util.List;

import com.nanuvem.lom.api.Instance;
import com.nanuvem.lom.api.Relation;
import com.nanuvem.lom.api.RelationType;

public interface RelationDao {
    Relation create(Relation relation);

    Relation findById(Long id);

    Relation update(Relation relation);

    List<Relation> listAllRelations();

    void delete(Long id);

    List<Relation> findRelationsBySourceInstance(Instance source, RelationType relationType);

    List<Relation> findRelationsByRelationType(RelationType relationType);

    List<Relation> findRelationsByTargetInstance(Instance targetInstance);
}
