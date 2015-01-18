package com.nanuvem.lom.api;

import java.util.List;

public interface Facade {

	//Entity
	
	Entity create(Entity entity);

	Entity findEntityById(Long id);

	Entity findEntityByFullName(String fullName);

	List<Entity> listAllEntities();

	List<Entity> listEntitiesByFullName(String fragment);
	
	Entity update(Entity entity);
	
	void deleteEntity(Long id);

	//Attribute
	
	Attribute create(Attribute attribute);

	Attribute findAttributeById(Long id);

	Attribute findAttributeByNameAndEntityFullName(String name,
			String fullEntityName);

	Attribute update(Attribute attribute);

	//Instance
	
	Instance create(Instance instance);

	Instance findInstanceById(Long id);

    List<Instance> findInstancesByEntityId(Long entityId);
    
    //RelationType
   
    RelationType create(RelationType relationType);

	RelationType findRelationTypeById(Long id);

	List<RelationType> findAllRelationTypes();
    
	
    
}
