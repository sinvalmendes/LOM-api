package com.nanuvem.lom.api.dao;

import com.nanuvem.lom.api.Instance;


public interface InstanceDao {

	Instance create(Instance instance);

	Instance findInstanceById(Long id);
	
	Instance update(Instance instance);

	void delete(Long id);
	
}
