package com.nanuvem.lom.api.dao;

import java.util.List;

import com.nanuvem.lom.api.Instance;

public interface InstanceDao {

    Instance create(Instance instance);

    Instance findInstanceById(Long id);

    List<Instance> findInstancesByEntityId(Long entityId);

    Instance update(Instance instance);

    void delete(Long id);

}
