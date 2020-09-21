package app.trigyn.core.menu.reposirtory.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import app.trigyn.core.menu.dao.QueryStore;
import app.trigyn.core.menu.entities.ModuleTargetLookup;
import app.trigyn.core.menu.vo.ModuleTargetLookupVO;

@Repository
public interface IModuleTargetLookupRepository extends JpaRepositoryImplementation<ModuleTargetLookup, Integer>{

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULE_TARGET_LOOKUP)
	List<ModuleTargetLookupVO> getAllModuleTargetLookup();
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_TARGET_LOOKUP_NAME)
	String getAllModuleTargetLookupName(Integer targetTypeId);
}
