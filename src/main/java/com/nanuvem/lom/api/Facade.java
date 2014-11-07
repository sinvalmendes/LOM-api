package com.nanuvem.lom.api;

import java.util.List;

public interface Facade {

	Entity create(Entity entity);

	Entity findEntityById(Long id);

	Entity findEntityByFullName(String fullName);

	List<Entity> listAllEntities();

	List<Entity> listEntitiesByFullName(String fragment);
	
	Entity update(Entity entity);
	
	void deleteEntity(Long id);

}
