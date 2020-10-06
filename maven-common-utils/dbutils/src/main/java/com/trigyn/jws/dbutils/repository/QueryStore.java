package com.trigyn.jws.dbutils.repository;

public final class QueryStore {

    private QueryStore() {

    }
    
    public static final String JPA_QUERY_TO_GET_MODULE_BY_MODULE_ID = "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
    		+ " (ml.moduleId AS moduleId, COALESCE(mlI18n.moduleName, mlI18nDef.moduleName) AS moduleName "
    		+ ", ml.moduleUrl AS moduleURL, ml.parentId AS parentModuleId, COALESCE(mlI18nP.moduleName, mlI18nPDef.moduleName) AS parentModuleName "
    		+ ", ml.sequence AS sequence, ml.targetLookupId AS targetLookupId, mtl.description AS targetLookupDesc, ml.targetTypeId AS targetTypeId) "
    		+ " FROM ModuleListing AS ml "
    		+ " LEFT OUTER JOIN ModuleListing AS mlp ON mlp.moduleId = ml.parentId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
    		+ " LEFT OUTER JOIN mlp.moduleListingI18ns AS mlI18nP ON mlI18nP.id.languageId = :languageId "
    		+ " LEFT OUTER JOIN mlp.moduleListingI18ns AS mlI18nPDef ON mlI18nPDef.id.languageId = :defaultLanguageId "
    		+ " LEFT OUTER JOIN ml.moduleTargetLookup AS mtl "
    		+ " WHERE ml.moduleId = :moduleId ";
    
    
    public static final String JPA_QUERY_TO_GET_ALL_MODULES = "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
    		+ " (ml.moduleId AS moduleId, COALESCE(mlI18n.moduleName, mlI18nDef.moduleName) AS moduleName ) "
    		+ " FROM ModuleListing AS ml "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
    		+ " WHERE ml.parentId IS NULL ";
    
    public static final String JPA_QUERY_TO_GET_ALL_MODULES_DETAILS = "SELECT new com.trigyn.jws.dbutils.vo.ModuleDetailsVO "
    		+ " (ml.moduleId AS moduleId, COALESCE(mlI18n.moduleName, mlI18nDef.moduleName) AS moduleName "
    		+ " , ml.moduleUrl AS moduleURL, ml.parentId AS parentModuleId "
    		+ " , COALESCE(mlI18nP.moduleName, mlI18nPDef.moduleName) AS parentModuleName, ml.sequence AS sequence"
    		+ " ,  COUNT(mls.moduleId) AS subModuleCount ) "
    		+ " FROM ModuleListing AS ml "
    		+ " LEFT OUTER JOIN ModuleListing AS mls ON ml.moduleId = mls.parentId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nP ON mlI18nP.id.languageId = :languageId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nPDef ON mlI18nPDef.id.languageId = :defaultLanguageId "
    		+ " GROUP BY ml.moduleId ORDER BY ml.sequence ASC ";
    
    public static final String JPA_QUERY_TO_GET_MODULE_ID_BY_NAME = "SELECT ml.moduleId AS moduleId"
    		+ " FROM ModuleListing AS ml "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId "
    		+ " WHERE mlI18n.moduleName = :moduleName OR mlI18nDef.moduleName = :moduleName ";
    
    public static final String JPA_QUERY_TO_GET_MODULE_ID_BY_SEQUENCE = "SELECT ml.moduleId AS moduleId"
    		+ " FROM ModuleListing AS ml "
    		+ " WHERE ml.parentId IS NULL AND ml.sequence = :sequence ";
    
    public static final String JPA_QUERY_TO_GET_MODULE_ID_BY_PARENT_SEQUENCE = "SELECT ml.moduleId AS moduleId "
    		+ " FROM ModuleListing AS ml "
    		+ " WHERE ml.parentId = :parentModuleId AND ml.sequence = :sequence ";
    
    public static final String JPA_QUERY_TO_GET_MODULE_ID_BY_URL = "SELECT ml.moduleId AS moduleId "
    		+ " FROM ModuleListing AS ml "
    		+ " WHERE ml.moduleUrl = :moduleURL ";
        
    public static final String JPA_QUERY_TO_GET_ALL_MODULE_TARGET_LOOKUP = "SELECT new com.trigyn.jws.dbutils.vo.ModuleTargetLookupVO "
    		+ " (mtl.lookupId AS lookupId, mtl.description AS description ) "
    		+ " FROM ModuleTargetLookup AS mtl";
    
    public static final String JPA_QUERY_TO_GET_MODULE_TARGET_LOOKUP_NAME = "SELECT mtl.description AS description "
    		+ " FROM ModuleTargetLookup AS mtl "
    		+ " WHERE mtl.lookupId = :targetTypeId ORDER BY description ASC ";
    
    
    public static final String JPA_QUERY_TO_GET_TARGET_TYPE_BY_URL = "SELECT  new com.trigyn.jws.dbutils.vo.ModuleDetailsVO( "
    		+ " ml.targetLookupId AS targetLookupId, ml.targetTypeId AS targetTypeId )"
    		+ " FROM ModuleListing AS ml "
    		+ " WHERE ml.moduleUrl = :moduleURL ";
    
    
}
