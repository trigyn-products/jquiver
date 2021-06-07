package com.trigyn.jws.dbutils.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;

@Repository
public interface IModuleListingRepository extends JpaRepositoryImplementation<ModuleListing, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_BY_MODULE_ID)
	ModuleDetailsVO getModuleDetails(String moduleId, Integer languageId, Integer defaultLanguageId);

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_PARENT_MODULES)
	List<ModuleDetailsVO> getAllParentModules(Integer isNotHomePage, Integer languageId, Integer defaultLanguageId, Integer isInsideMenu);

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULES_DETAILS)
	List<ModuleDetailsVO> getAllModulesDetails(Integer isNotHomePage, Integer languageId, Integer defaultLanguageId);

	@Query(QueryStore.JPA_QUERY_TO_GET_ROLE_SPECIFIC_MENU_MODULES_DETAILS)
	List<ModuleDetailsVO> getRoleSpecificModulesDetails(Integer isNotHomePage, Integer languageId, Integer defaultLanguageId,
		List<String> roleList, String jeraModuleID);

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_NAME)
	String getModuleIdByName(String moduleName, Integer languageId, Integer defaultLanguageId);

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_SEQUENCE)
	String getModuleIdBySequence(Integer sequence);

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_PARENT_SEQUENCE)
	String getParentModuleIdBySequence(String parentModuleId, Integer sequence);

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULE_ID)
	List<ModuleDetailsVO> getAllModuleId(String moduleId);

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_URL)
	List<ModuleDetailsVO> getModuleIdByURL(String moduleURL, String moduleId);

	@Query(QueryStore.JPA_QUERY_TO_GET_TARGET_TYPE_BY_URL)
	ModuleDetailsVO getTargetTypeByURL(String moduleURL);

	@Query(QueryStore.JPA_QUERY_TO_GET_TARGET_TYPE_FOR_URL)
	List<ModuleDetailsVO> getTargetTypeURL(String moduleURL);

	@Query(" FROM ModuleListing WHERE moduleId=:moduleId")
	ModuleListing getModuleListing(String moduleId);

	@Query("FROM ModuleListing ml WHERE moduleId = :moduleId AND moduleId NOT IN(SELECT mra.moduleId FROM ModuleRoleAssociation AS mra WHERE mra.moduleId = :moduleId AND mra.isDeleted = 0)")
	ModuleListing getModuleListingByRole(String moduleId);

	@Query(QueryStore.JPA_QUERY_TO_GET_HOME_PAGE_URL_BY_ROLE_IDS)
	List<String> getModuleURLByRoleId(List<String> roleIdList, Integer isHomePage);

}
