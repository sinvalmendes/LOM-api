package com.nanuvem.lom.api.tests.relation;

import com.nanuvem.lom.api.Facade;

public class RelationHelper {
	private static Facade facade;

	public static void setFacade(Facade facade) {
		RelationHelper.facade = facade;
	}
}
