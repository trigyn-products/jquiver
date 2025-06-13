package com.trigyn.jws.dbutils.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.JwsModuleVersion;
import com.trigyn.jws.dbutils.utils.QueryStore;
import com.trigyn.jws.dbutils.vo.ModuleVersionVO;

public interface JwsTemplateVersionRepository extends JpaRepositoryImplementation<JwsModuleVersion, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULE_VERSION)
	List<ModuleVersionVO> getModuleVersionById(@Param("entityId") String entityId, @Param("entityName") String entityName);

}
