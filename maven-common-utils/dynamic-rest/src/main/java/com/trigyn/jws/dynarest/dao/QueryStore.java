package com.trigyn.jws.dynarest.dao;

public final class QueryStore {

	private QueryStore() {

	}

	protected static final String	QUERY_TO_ALL_API_QUERIES				= "SELECT new com.trigyn.jws.dynarest.vo.RestApiDaoQueries(jdrdd.jwsDaoQueryTemplate , jdrdd.jwsResultVariableName, jdrdd.jwsQuerySequence, jdrdd.queryType) FROM JwsDynamicRestDaoDetail AS jdrdd "
			+ " WHERE jdrdd.jwsDynamicRestDetailId = :dynarestId ORDER BY jdrdd.jwsQuerySequence ASC ";

	protected static final String	QUERY_TO_API_DETAILS_BY_URL				= "SELECT new com.trigyn.jws.dynarest.vo.RestApiDetails(jdrd.jwsDynamicRestId, jdrd.jwsDynamicRestUrl, jdrd.jwsRbacId, jdrd.jwsMethodName, "
			+ " jdrd.jwsMethodDescription, jrpd.jwsResponseProducerType, jdrd.jwsServiceLogic, jdrd.jwsPlatformId, jrtd.jwsRequestType) FROM JwsDynamicRestDetail AS jdrd LEFT OUTER JOIN jdrd.jwsRequestTypeDetail AS jrtd LEFT OUTER JOIN jdrd.jwsResponseProducerDetail AS jrpd "
			+ " WHERE jdrd.jwsDynamicRestUrl = :jwsDynamicRestUrl GROUP BY jdrd.jwsDynamicRestId ";

	protected static final String	QUERY_TO_GET_DYNAMIC_REST_ID			= "SELECT jdrd.jwsDynamicRestId AS dynamicRestId "
			+ " FROM JwsDynamicRestDetail AS jdrd "
			+ " WHERE jdrd.jwsDynamicRestUrl = :jwsDynamicRestUrl AND jdrd.jwsMethodName = :jwsMethodName ";

	public static final String		QUERY_TO_GET_JAVA_DYNAMIC_REST_DETAILS	= "SELECT new com.trigyn.jws.dynarest.vo.RestApiDetails(jdrd.jwsDynamicRestId, jdrd.jwsDynamicRestUrl, jdrd.jwsRbacId, jdrd.jwsMethodName, "
			+ " jdrd.jwsMethodDescription, jrpd.jwsResponseProducerType, jdrd.jwsServiceLogic, jdrd.jwsPlatformId, jrtd.jwsRequestType) FROM JwsDynamicRestDetail AS jdrd LEFT OUTER JOIN jdrd.jwsRequestTypeDetail AS jrtd LEFT OUTER JOIN jdrd.jwsResponseProducerDetail AS jrpd "
			+ " WHERE jdrd.jwsPlatformId = 1 GROUP BY jdrd.jwsDynamicRestId ";

}
