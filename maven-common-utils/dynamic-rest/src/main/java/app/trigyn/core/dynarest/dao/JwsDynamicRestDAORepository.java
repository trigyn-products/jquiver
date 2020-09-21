package app.trigyn.core.dynarest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import app.trigyn.core.dynarest.entities.JwsDynamicRestDaoDetail;
import app.trigyn.core.dynarest.vo.RestApiDaoQueries;

@Repository
public interface JwsDynamicRestDAORepository extends JpaRepositoryImplementation<JwsDynamicRestDaoDetail, Integer>{
    
    @Query(QueryStore.QUERY_TO_ALL_API_QUERIES)
    List<RestApiDaoQueries> getRestApiDaoQueriesByApiId(Integer dynarestId);
}
