package com.trigyn.jws.dbutils.repository;

public final class QueryStore {

	private QueryStore() {

	}

	public static final String	JPA_QUERY_TO_GET_MODULE_BY_MODULE_ID				= "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
			+ " (ml.moduleId AS moduleId, COALESCE(mlI18n.moduleName, mlI18nDef.moduleName) AS moduleName "
			+ ", ml.moduleUrl AS moduleURL, ml.parentId AS parentModuleId, COALESCE(mlI18nP.moduleName, mlI18nPDef.moduleName) AS parentModuleName "
			+ ", ml.sequence AS sequence, ml.isInsideMenu AS isInsideMenu, ml.includeLayout AS includeLayout, ml.targetLookupId AS targetLookupId"
			+ ", mtl.description AS targetLookupDesc, ml.targetTypeId AS targetTypeId, ml.headerJson AS headerJson, ml.requestParamJson AS requestParamJson, ml.openInNewTab AS openInNewTab, ml.menuStyle AS menuStyle, ml.isHomePage AS isHomePage) "
			+ " FROM ModuleListing AS ml " + " LEFT OUTER JOIN ModuleListing AS mlp ON mlp.moduleId = ml.parentId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
			+ " LEFT OUTER JOIN mlp.moduleListingI18ns AS mlI18nP ON mlI18nP.id.languageId = :languageId "
			+ " LEFT OUTER JOIN mlp.moduleListingI18ns AS mlI18nPDef ON mlI18nPDef.id.languageId = :defaultLanguageId "
			+ " LEFT OUTER JOIN ml.moduleTargetLookup AS mtl " + " WHERE ml.moduleId = :moduleId ";

	public static final String	JPA_QUERY_TO_GET_ALL_PARENT_MODULES					= "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
			+ " (ml.moduleId AS moduleId, COALESCE(mlI18n.moduleName, mlI18nDef.moduleName) AS moduleName, ml.moduleUrl AS moduleURL) "
			+ " FROM ModuleListing AS ml " + " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
			+ " WHERE ml.parentId IS NULL AND ml.isHomePage = :isNotHomePage AND ml.isInsideMenu = :isInsideMenu ORDER BY mlI18n.moduleName ASC";

	public static final String	JPA_QUERY_TO_GET_ALL_MODULES_DETAILS				= "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
			+ " (ml.moduleId AS moduleId, COALESCE(mlI18n.moduleName, mlI18nDef.moduleName) AS moduleName "
			+ " , ml.moduleUrl AS moduleURL, ml.parentId AS parentModuleId "
			+ " , COALESCE(mlI18nP.moduleName, mlI18nPDef.moduleName) AS parentModuleName, ml.sequence AS sequence"
			+ " ,  COUNT(mls.moduleId) AS subModuleCount, ml.targetTypeId AS targetTypeId, ml.moduleTargetLookup.lookupId AS targetLookupId, ml.requestParamJson AS requestParamJson, ml.openInNewTab AS openInNewTab, ml.menuStyle AS menuStyle, ml.isHomePage AS isHomePage)" 
			+ " FROM ModuleListing AS ml "
			+ " LEFT OUTER JOIN ModuleListing AS mls ON ml.moduleId = mls.parentId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nP ON mlI18nP.id.languageId = :languageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nPDef ON mlI18nPDef.id.languageId = :defaultLanguageId "
			+ " WHERE ml.sequence IS NOT NULL AND ml.isInsideMenu = 1 " + " GROUP BY ml.moduleId ORDER BY ml.sequence ASC ";

	public static final String	JPA_QUERY_TO_GET_ROLE_SPECIFIC_MENU_MODULES_DETAILS	= "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
			+ " (ml.moduleId AS moduleId, COALESCE(mlI18n.moduleName, mlI18nDef.moduleName) AS moduleName "
			+ " , ml.moduleUrl AS moduleURL, ml.parentId AS parentModuleId "
			+ " , COALESCE(mlI18nP.moduleName, mlI18nPDef.moduleName) AS parentModuleName, ml.sequence AS sequence"
			+ " ,  COUNT(mls.moduleId) AS subModuleCount, ml.targetTypeId AS targetTypeId, ml.moduleTargetLookup.lookupId AS targetLookupId, ml.requestParamJson AS requestParamJson, ml.openInNewTab AS openInNewTab, ml.menuStyle AS menuStyle, ml.isHomePage AS isHomePage) " 
			+ " FROM ModuleListing AS ml "
			+ " LEFT OUTER JOIN ModuleListing AS mls ON ml.moduleId = mls.parentId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nP ON mlI18nP.id.languageId = :languageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nPDef ON mlI18nPDef.id.languageId = :defaultLanguageId "
			+ " LEFT OUTER JOIN JwsEntityRoleAssociation jera ON jera.entityId = ml.moduleId "
			+ " LEFT OUTER JOIN JwsRole jr ON jera.roleId = jr.roleId " + " WHERE ml.sequence IS NOT NULL AND ml.isInsideMenu = 1 "
			+ " AND jr.roleName IN :roleList AND jera.moduleId = :jeraModuleID AND jera.isActive = 1"
			+ " GROUP BY ml.moduleId ORDER BY ml.sequence ASC ";

	public static final String	JPA_QUERY_TO_GET_MODULE_ID_BY_NAME					= "SELECT ml.moduleId AS moduleId"
			+ " FROM ModuleListing AS ml " + " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
			+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
			+ " WHERE mlI18n.moduleName = :moduleName OR mlI18nDef.moduleName = :moduleName ";

	public static final String	JPA_QUERY_TO_GET_MODULE_ID_BY_SEQUENCE				= "SELECT ml.moduleId AS moduleId"
			+ " FROM ModuleListing AS ml " + " WHERE ml.parentId IS NULL AND ml.sequence = :sequence ";

	public static final String	JPA_QUERY_TO_GET_MODULE_ID_BY_PARENT_SEQUENCE		= "SELECT ml.moduleId AS moduleId "
			+ " FROM ModuleListing AS ml " + " WHERE ml.parentId = :parentModuleId AND ml.sequence = :sequence ";

	public static final String	JPA_QUERY_TO_GET_ALL_MODULE_ID						= "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
			+ " (ml.moduleId AS moduleId, mlI18n.moduleName, ml.moduleUrl) FROM ModuleListing AS ml "
			+ " INNER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = 1 " + " WHERE ml.moduleId != :moduleId ";

	public static final String	JPA_QUERY_TO_GET_MODULE_ID_BY_URL					= "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
			+ " (ml.moduleId AS moduleId, mlI18n.moduleName, ml.moduleUrl) FROM ModuleListing AS ml "
			+ " INNER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = 1 "
			+ " WHERE ml.moduleUrl LIKE :moduleURL AND ml.moduleId != :moduleId ";

	public static final String	JPA_QUERY_TO_GET_ALL_MODULE_TARGET_LOOKUP			= "SELECT new com.trigyn.jws.dbutils.vo.ModuleTargetLookupVO "
			+ " (mtl.lookupId AS lookupId, mtl.description AS description ) " + " FROM ModuleTargetLookup AS mtl";

	public static final String	JPA_QUERY_TO_GET_MODULE_TARGET_LOOKUP_NAME			= "SELECT mtl.description AS description "
			+ " FROM ModuleTargetLookup AS mtl " + " WHERE mtl.lookupId = :targetTypeId ORDER BY description ASC ";

	public static final String	JPA_QUERY_TO_GET_TARGET_TYPE_BY_URL					= "SELECT  new com.trigyn.jws.dbutils.vo.ModuleDetailsVO( "
			+ " ml.moduleUrl AS moduleUrl, ml.targetLookupId AS targetLookupId, ml.targetTypeId AS targetTypeId,"
			+ " ml.includeLayout AS includeLayout, ml.headerJson AS headerJson, ml.requestParamJson AS requestParamJson )" + " FROM ModuleListing AS ml "
			+ " WHERE ml.moduleUrl = :moduleURL ";

	public static final String	JPA_QUERY_TO_GET_TARGET_TYPE_FOR_URL				= "SELECT  new com.trigyn.jws.dbutils.vo.ModuleDetailsVO( "
			+ " ml.moduleUrl AS moduleUrl, ml.targetLookupId AS targetLookupId, ml.targetTypeId AS targetTypeId"
			+ ", ml.includeLayout AS includeLayout, ml.headerJson AS headerJson, ml.requestParamJson AS requestParamJson )" + " FROM ModuleListing AS ml "
			+ " WHERE ml.moduleUrl LIKE %:moduleURL% ";

	public static final String	JPA_QUERY_TO_GET_LOOKUP_DETAILS						= "SELECT new com.trigyn.jws.dbutils.vo.LookupDetailsVO "
			+ " (jl.lookupName AS lookupName, jl.recordId AS recordId "
			+ ", COALESCE(jli18n.languageId, jli18nDef.languageId) AS languageId "
			+ ", COALESCE(jli18n.recordDescription, jli18nDef.recordDescription) AS recordDescription) FROM JwsLookup AS jl "
			+ " LEFT OUTER JOIN jl.jwsLookupI18ns AS jli18n ON jli18n.languageId = :languageId "
			+ " LEFT OUTER JOIN jl.jwsLookupI18ns AS jli18nDef ON jli18nDef.languageId = :defaultLanguageId "
			+ " WHERE jl.lookupName = :lookupName AND jl.isDeleted = :isDeleted ORDER BY recordId ASC";

	public static final String	JPA_QUERY_TO_GET_HOME_PAGE_URL_BY_ROLE_IDS			= "SELECT  ml.moduleUrl AS moduleUrl "
			+ " FROM ModuleListing AS ml "
			+ " INNER JOIN ml.moduleRoleAssociations AS mra ON mra.roleId IN (:roleIdList) AND mra.isDeleted = 0 "
			+ " INNER JOIN JwsRole AS jr ON jr.roleId = mra.roleId AND jr.isActive = 1"
			+ " WHERE ml.isHomePage = :isHomePage ORDER BY jr.rolePriority DESC ";

	public static final String	JPA_QUERY_TO_MODULE_ROLE_BY_ID						= "SELECT mra.moduleId AS moduleId "
			+ " FROM ModuleRoleAssociation AS mra " + " WHERE mra.roleId <> :roleId AND mra.moduleId = :moduleId ";

	public static final String	JPA_QUERY_TO_GET_IS_HOME_PAGE_BY_URL			= "SELECT  ml.isHomePage AS isHomePage "
			+ " FROM ModuleListing AS ml "
			+ " WHERE ml.targetLookupId = 5 "
			+ " AND ml.targetTypeId = (SELECT templateId FROM TemplateMaster WHERE templateName = :targetName) ";

}
