package com.nanuvem.lom.api;

import java.util.List;

public interface Facade {

    // Entity

    EntityType create(EntityType entityType);

    EntityType findEntityById(Long id);

    EntityType findEntityByFullName(String fullName);

    List<EntityType> listAllEntities();

    List<EntityType> listEntitiesByFullName(String fragment);

    EntityType update(EntityType entityType);

    void deleteEntity(Long id);

    // Attribute

    PropertyType create(PropertyType propertyType);

    PropertyType findAttributeById(Long id);

    PropertyType findAttributeByNameAndEntityFullName(String name, String fullEntityName);

    PropertyType update(PropertyType propertyType);

    // Instance

    Entity create(Entity entity);

    Entity findInstanceById(Long id);

    List<Entity> findInstancesByEntityId(Long entityId);

    // RelationType

    RelationType create(RelationType relationType);

    RelationType findRelationTypeById(Long id);

    List<RelationType> listAllRelationTypes();

    RelationType update(RelationType relationType);

    void deleteRelationType(Long id);

    // Relation

    Relation create(Relation relation);

    Relation findRelationById(Long id);

    List<Relation> listAllRelations();

    Relation update(Relation relation);

    void deleteRelation(Long id);

    List<Relation> findRelationsBySourceInstance(Entity source, RelationType relationType);

    List<Relation> findRelationsByRelationType(RelationType relationType);

}
