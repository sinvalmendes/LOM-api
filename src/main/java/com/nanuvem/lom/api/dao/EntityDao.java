package com.nanuvem.lom.api.dao;

import java.util.List;

import com.nanuvem.lom.api.Entity;

public interface EntityDao {

    Entity create(Entity entity);

    List<Entity> listAll();

    Entity findById(Long id);

    List<Entity> listByFullName(String fragment);

    Entity findByFullName(String fullName);

    Entity update(Entity entity);

    void delete(Long id);
}
