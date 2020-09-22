package com.trigyn.jws.menu.reposirtory.interfaces;

import java.util.List;

import com.trigyn.jws.menu.dao.QueryStore;
import com.trigyn.jws.menu.entities.ModuleTargetLookup;
import com.trigyn.jws.menu.vo.ModuleTargetLookupVO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface IModuleTargetLookupRepository extends JpaRepositoryImplementation<ModuleTargetLookup, Integer>{

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULE_TARGET_LOOKUP)
	List<ModuleTargetLookupVO> getAllModuleTargetLookup();
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_TARGET_LOOKUP_NAME)
	String getAllModuleTargetLookupName(Integer targetTypeId);
}
