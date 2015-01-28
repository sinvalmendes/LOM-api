package com.nanuvem.lom.api.dao;

import java.util.List;

import com.nanuvem.lom.api.Attribute;
import com.nanuvem.lom.api.RelationType;

public interface RelationTypeDao {
	RelationType create(RelationType relationType);

	RelationType findRelationTypeById(Long id);

	Attribute update(RelationType relationType);

	List<RelationType> listAllRelationTypes();
}
