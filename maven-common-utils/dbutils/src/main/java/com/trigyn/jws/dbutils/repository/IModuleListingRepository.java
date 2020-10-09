package com.trigyn.jws.dbutils.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;

@Repository
public interface IModuleListingRepository extends JpaRepositoryImplementation<ModuleListing, Integer>{

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_BY_MODULE_ID)
	ModuleDetailsVO getModuleDetails(String moduleId, Integer languageId, Integer defaultLanguageId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_PARENT_MODULES)
	List<ModuleDetailsVO> getAllParentModules(String homeModuleURL,Integer languageId, Integer defaultLanguageId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULES_DETAILS)
	List<ModuleDetailsVO> getAllModulesDetails(String homeModuleURL, Integer languageId, Integer defaultLanguageId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_NAME)
	String getModuleIdByName(String moduleName, Integer languageId, Integer defaultLanguageId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_SEQUENCE)
	String getModuleIdBySequence(Integer sequence);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_PARENT_SEQUENCE)
	String getParentModuleIdBySequence(String parentModuleId, Integer sequence);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_URL)
	String getModuleIdByURL(String moduleURL);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_TARGET_TYPE_BY_URL)
	ModuleDetailsVO getTargetTypeDetails(String moduleURL);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_HOME_PAGE_MODULE_ID)
	String getHomeModuleId(String homeModuleURL, Integer homeModuleSequence);
	
}
