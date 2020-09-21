package app.trigyn.core.menu.dao;

public final class QueryStore {

    private QueryStore() {

    }
    
    public static final String JPA_QUERY_TO_GET_MODULE_BY_MODULE_ID = "SELECT new app.trigyn.core.menu.vo.ModuleDetailsVO "
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
    
    
    public static final String JPA_QUERY_TO_GET_ALL_MODULES = "SELECT new app.trigyn.core.menu.vo.ModuleDetailsVO "
    		+ " (ml.moduleId AS moduleId, COALESCE(mlI18n.moduleName, mlI18nDef.moduleName) AS moduleName ) "
    		+ " FROM ModuleListing AS ml "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = :languageId "
    		+ " LEFT OUTER JOIN ml.moduleListingI18ns AS mlI18nDef ON mlI18nDef.id.languageId = :defaultLanguageId ";
    
    public static final String JPA_QUERY_TO_GET_ALL_MODULES_DETAILS = "SELECT new app.trigyn.core.menu.vo.ModuleDetailsVO "
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
    		+ " GROUP BY ml.moduleId ";
    
    public static final String JPA_QUERY_TO_GET_MODULE_SEQUENCE = "SELECT ml.moduleId AS moduleId"
    		+ " FROM ModuleListing AS ml "
    		+ " WHERE ml.sequence = :sequence ";
    
    public static final String JPA_QUERY_TO_GET_MODULE_SEQUENCE_BY_PARENT = "SELECT ml.moduleId AS moduleId "
    		+ " FROM ModuleListing AS ml "
    		+ " WHERE ml.parentId = :parentModuleId AND ml.sequence = :sequence ";
    
    public static final String JPA_QUERY_TO_GET_ALL_MODULE_TARGET_LOOKUP = "SELECT new app.trigyn.core.menu.vo.ModuleTargetLookupVO "
    		+ " (mtl.lookupId AS lookupId, mtl.description AS description ) "
    		+ " FROM ModuleTargetLookup AS mtl";
    
    public static final String JPA_QUERY_TO_GET_MODULE_TARGET_LOOKUP_NAME = "SELECT mtl.description AS description "
    		+ " FROM ModuleTargetLookup AS mtl "
    		+ " WHERE mtl.lookupId = :targetTypeId ORDER BY description ASC ";
    
    
    
}
