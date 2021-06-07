package com.trigyn.jws.dbutils.utils;

public final class QueryStore {

	public static final String	JPA_QUERY_TO_GET_ALL_ACTIVE_ROLES				= "SELECT new com.trigyn.jws.dbutils.vo.UserRoleVO"
			+ "(ur.roleId AS roleId, ur.roleName AS roleName, ur.roleDescription AS roleDescription) FROM UserRole as ur "
			+ " WHERE ur.isDeleted = :isDeleted ORDER BY ur.roleName ASC ";

	public static final String	JPA_QUERY_TO_GET_ALL_MODULE_VERSION				= "SELECT new com.trigyn.jws.dbutils.vo.ModuleVersionVO"
			+ "(jmv.moduleVersionId AS moduleVersionId, jmv.entityId AS entityId"
			+ ", jmv.moduleJson AS moduleJson , jmv.versionId AS versionId"
			+ ", jmv.updatedDate AS updatedDate, jmv.moduleJsonChecksum AS moduleJsonChecksum"
			+ ", jmv.sourceTypeId AS sourceTypeId) FROM JwsModuleVersion AS jmv "
			+ " WHERE jmv.entityId = :entityId AND jmv.entityName = :entityName ORDER BY jmv.versionId ASC ";

	public static final String	JPA_QUERY_TO_GET_DRIVER_CLASS_NAME_BY_ID		= "SELECT dlu.driverClassName "
			+ " FROM DatasourceLookUp AS dlu WHERE datasourceLookupId = :datasourceLookupId ";

	public static final String	JPA_QUERY_TO_GET_DATASOURCE_PRODUCT_NAME_BY_ID	= "SELECT dlu.databaseProductName "
			+ " FROM AdditionalDatasource AS ad INNER JOIN DatasourceLookUp AS dlu ON ad.datasourceLookupId = dlu.datasourceLookupId"
			+ " WHERE ad.additionalDatasourceId = :additionalDatasourceId ";

}
