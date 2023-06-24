package com.trigyn.jws.dynarest.dao;

public final class QueryStore {

	private QueryStore() {

	}

	protected static final String	QUERY_TO_ALL_API_QUERIES															= "SELECT new com.trigyn.jws.dynarest.vo.RestApiDaoQueries(jdrdd.jwsDaoQueryTemplate , jdrdd.jwsResultVariableName, jdrdd.jwsQuerySequence, jdrdd.queryType, jdrdd.datasourceId) FROM JwsDynamicRestDaoDetail AS jdrdd "
			+ " WHERE jdrdd.jwsDynamicRestDetailId = :dynarestId ORDER BY jdrdd.jwsQuerySequence ASC ";

	protected static final String	QUERY_TO_API_DETAILS_BY_URL															= "SELECT new com.trigyn.jws.dynarest.vo.RestApiDetails(jdrd.jwsDynamicRestId, jdrd.jwsDynamicRestUrl, jdrd.jwsRbacId, jdrd.jwsMethodName, "
			+ " jdrd.jwsMethodDescription, jrpd.jwsResponseProducerType, jdrd.jwsServiceLogic, jdrd.jwsPlatformId, jrtd.jwsRequestType, jdrd.jwsHeaderJson, jdrd.isSecured, jdrd.jwsRequestTypeId) FROM JwsDynamicRestDetail AS jdrd LEFT OUTER JOIN jdrd.jwsRequestTypeDetail AS jrtd LEFT OUTER JOIN jdrd.jwsResponseProducerDetail AS jrpd "
			+ " WHERE jdrd.jwsDynamicRestUrl = :jwsDynamicRestUrl GROUP BY jdrd.jwsDynamicRestId ";

	protected static final String	QUERY_TO_GET_DYNAMIC_REST_ID														= "SELECT jdrd.jwsDynamicRestId AS dynamicRestId "
			+ " FROM JwsDynamicRestDetail AS jdrd "
			+ " WHERE jdrd.jwsDynamicRestUrl = :jwsDynamicRestUrl AND jdrd.jwsMethodName = :jwsMethodName ";

	public static final String		QUERY_TO_GET_JAVA_DYNAMIC_REST_DETAILS												= "SELECT new com.trigyn.jws.dynarest.vo.RestApiDetails(jdrd.jwsDynamicRestId, jdrd.jwsDynamicRestUrl, jdrd.jwsRbacId, jdrd.jwsMethodName, "
			+ " jdrd.jwsMethodDescription, jrpd.jwsResponseProducerType, jdrd.jwsServiceLogic, jdrd.jwsPlatformId, jrtd.jwsRequestType, jdrd.jwsHeaderJson, jdrd.isSecured) FROM JwsDynamicRestDetail AS jdrd LEFT OUTER JOIN jdrd.jwsRequestTypeDetail AS jrtd LEFT OUTER JOIN jdrd.jwsResponseProducerDetail AS jrpd "
			+ " WHERE jdrd.jwsPlatformId = 1 GROUP BY jdrd.jwsDynamicRestId ";

	public static final String		QUERY_TO_API_DETAILS_BY_ID															= "SELECT new com.trigyn.jws.dynarest.vo.RestApiDetails(jdrd.jwsDynamicRestId, jdrd.jwsDynamicRestUrl, jdrd.jwsRbacId, jdrd.jwsMethodName, "
			+ " jdrd.jwsMethodDescription, jdrd.jwsResponseProducerTypeId, jdrd.jwsServiceLogic, jdrd.jwsPlatformId, jrtd.jwsRequestType, jdrd.jwsHeaderJson, jdrd.isSecured) FROM JwsDynamicRestDetail AS jdrd LEFT OUTER JOIN jdrd.jwsRequestTypeDetail AS jrtd "
			+ " WHERE jdrd.jwsDynamicRestId = :jwsDynamicRestId  ";

	public static final String		JPA_QUERY_TO_GET_FILE_DETAILS_ID_BY_FILE_UPLOAD_ID									= "SELECT fu FROM FileUpload AS fu WHERE fu.fileUploadId = :fileUploadId";

	public static final String		JPA_QUERY_TO_GET_ALL_FILE_DETAILS_BY_FILE_UPLOAD_ID									= "SELECT fu FROM FileUpload AS fu WHERE fu.fileUploadId IN ( :fileUploadIdList ) ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC ";

	public static final String		JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_BIN_ID_AND_ASSOC_ID							= "SELECT fu FROM FileUpload AS fu WHERE fu.fileBinId = :fileBinId AND fu.fileAssociationId = :fileAssociationId ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC";

	public static final String		JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_ASSOC_ID										= "SELECT fu FROM FileUpload AS fu WHERE fu.fileAssociationId = :fileAssociationId ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC";

	public static final String		JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_BIN_ID										= "SELECT fu FROM FileUpload AS fu WHERE fu.fileBinId = :fileBinId ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC";

	public static final String		JPA_QUERY_TO_GET_ACTIVE_SCHEDULERS													= "SELECT sc FROM JqScheduler AS sc WHERE sc.isActive = :isActive";

	public static final String		JPA_QUERY_TO_GET_ALL_TEMP_FILE_DETAILS_BY_FILE_UPLOAD_ID							= "SELECT fu FROM FileUploadTemp AS fu WHERE fu.fileUploadId = :fileUploadId AND action = 1";

	public static final String		JPA_QUERY_TO_GET_FILE_TEMP_DETAILS_BY_FILE_BIN_ID									= "SELECT fu FROM FileUploadTemp AS fu WHERE fu.fileBinId = :fileBinId ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC";

	public static final String		JPA_QUERY_TO_GET_FILE_TEMP_DETAILS_BY_FILE_BIN_ID_FILE_TEMP_ASSOC_ID_FILE_UPLOAD_ID	= "SELECT fut FROM FileUploadTemp AS fut WHERE fut.fileAssociationId = :fileAssociationId AND fut.fileBinId = :fileBinId AND fut.fileUploadId = :fileUploadId AND fut.action = -1";

	public static final String		JPA_QUERY_TO_GET_FILE_TEMP_DETAILS_BY_FILE_BIN_ID_FILE_TEMP_ASSOC_ID_TEMP_ID		= "SELECT fut FROM FileUploadTemp AS fut WHERE fut.fileAssociationId = :fileAssociationId AND fut.fileBinId = :fileBinId AND fut.fileUploadTempId = :fileUploadTempId AND fut.action = -1";

	public static final String		JPA_QUERY_TO_GET_ACTIVE_CLUSTER_STATE												= "SELECT jcs FROM JwsClusterState AS jcs WHERE jcs.isActive = :isActive";

	public static final String		JPA_QUERY_TO_GET_ACTIVE_CLUSTER_STATE_BY_CLUSTER									= "SELECT jcs FROM JwsClusterState AS jcs WHERE jcs.isSchedular = :isActive AND jcs.instanceId = :instanceId";

}