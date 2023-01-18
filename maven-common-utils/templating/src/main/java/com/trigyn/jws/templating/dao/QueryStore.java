package com.trigyn.jws.templating.dao;

public final class QueryStore {

	protected static final String	JPA_QUERY_TO_GET_TEMPALTE_DETAILS		= "SELECT new com.trigyn.jws.templating.vo.TemplateVO(tm.templateId, tm.templateName, tm.template, tm.updatedDate) FROM TemplateMaster as tm "
			+ " WHERE tm.templateName = :vmName ";

	protected static final String	HQL_QUERY_TO_GET_BY_ID					= "SELECT otm.templateName AS vmName, otm.template AS vmtemplate, otm.createdBy AS createdBy"
			+ " FROM TemplateMaster otm WHERE otm.vmMasterId=:vmMasterId";

	protected static final String	HQL_QUERY_TO_CHECK_NAME					= "SELECT otm.templateId FROM TemplateMaster otm  WHERE lower(otm.templateName) = lower(:vmName)";

	protected static final String	HQL_QUERY_TO_GET_VMTEMPLATE_BY_VMNAME	= " FROM TemplateMaster otm  WHERE lower(otm.templateName) = lower(:templateName)";

	protected static final String	JPA_QUERY_TO_GET_ALL_DEFAULT_TEMPLATES	= " SELECT new com.trigyn.jws.templating.vo.TemplateVO(tm.templateId, tm.templateName, tm.template, tm.checksum, tm.templateTypeId)"
			+ " FROM TemplateMaster as tm " + " WHERE tm.templateTypeId = :defaultTemplateTypeId ";
}