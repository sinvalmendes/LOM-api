package com.nanuvem.lom.api.dao;

import java.util.List;

import com.nanuvem.lom.api.Entity;

public interface InstanceDao {

    Entity create(Entity entity);

    Entity findInstanceById(Long id);

    List<Entity> findInstancesByEntityId(Long entityId);

    Entity update(Entity entity);

    void delete(Long id);

}
