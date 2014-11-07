package com.nanuvem.lom.api.dao;

import com.nanuvem.lom.api.Attribute;

public interface AttributeDao {

	Attribute create(Attribute attribute);

	Attribute findAttributeById(Long id);

	Attribute findAttributeByNameAndEntityFullName(String nameAttribute,
			String classFullName);

	Attribute update(Attribute attribute);

}
