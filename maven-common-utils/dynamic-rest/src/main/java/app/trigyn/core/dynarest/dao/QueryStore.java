package app.trigyn.core.dynarest.dao;

public final class QueryStore {
    
    private QueryStore() {

    }

    protected static final String QUERY_TO_ALL_API_QUERIES = "SELECT new app.trigyn.core.dynarest.vo.RestApiDaoQueries(jdrdd.jwsDaoQueryTemplate , jdrdd.jwsResultVariableName, jdrdd.jwsQuerySequence) FROM JwsDynamicRestDaoDetail AS jdrdd " 
                    + " WHERE jdrdd.jwsDynamicRestDetailId = :dynarestId ORDER BY jdrdd.jwsQuerySequence ASC ";

    protected static final String QUERY_TO_API_DETAILS_BY_URL = "SELECT new app.trigyn.core.dynarest.vo.RestApiDetails(jdrd.jwsDynamicRestId, jdrd.jwsDynamicRestUrl, jdrd.jwsRbacId, jdrd.jwsMethodName, "
            + " jdrd.jwsMethodDescription, jrpd.jwsResponseProducerType, jdrd.jwsServiceLogic, jdrd.jwsPlatformId, jrtd.jwsRequestType) FROM JwsDynamicRestDetail AS jdrd LEFT OUTER JOIN jdrd.jwsRequestTypeDetail AS jrtd LEFT OUTER JOIN jdrd.jwsResponseProducerDetail AS jrpd " 
            + " WHERE jdrd.jwsDynamicRestUrl = :jwsDynamicRestUrl GROUP BY jdrd.jwsDynamicRestId ";

}
