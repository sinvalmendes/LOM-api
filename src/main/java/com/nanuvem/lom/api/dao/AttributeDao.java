package com.nanuvem.lom.api.dao;

import com.nanuvem.lom.api.PropertyType;

public interface AttributeDao {

    PropertyType create(PropertyType propertyType);

    PropertyType findAttributeById(Long id);

    PropertyType findAttributeByNameAndEntityFullName(String nameAttribute, String classFullName);

    PropertyType update(PropertyType propertyType);

}
