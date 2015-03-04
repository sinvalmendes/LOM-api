package com.nanuvem.lom.api.dao;

import java.util.List;

import com.nanuvem.lom.api.EntityType;

public interface EntityDao {

    EntityType create(EntityType entityType);

    List<EntityType> listAll();

    EntityType findById(Long id);

    List<EntityType> listByFullName(String fragment);

    EntityType findByFullName(String fullName);

    EntityType update(EntityType entityType);

    void delete(Long id);
}
