package com.trigyn.jws.formio.dao;

public final class QueryStore {

	public static final String JPA_QUERY_TO_GET_FORM_IO_FORMS_BY_DYNA_FORM_ID = "SELECT fio FROM DynamicForm df LEFT OUTER JOIN lg.resourceBundles AS rb ON rb.id.resourceKey = :resourceBundleKe";
	
	public static final String HQL_QUERY_TO_DELETE_FORMIO_PAGE_ROLES = null;
	
}