package com.nanuvem.lom.api.dao;

public interface DaoFactory {

	EntityDao createEntityDao();

	AttributeDao createAttributeDao();

	InstanceDao createInstanceDao();

	AttributeValueDao createAttributeValueDao();
}
