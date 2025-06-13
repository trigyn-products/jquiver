package com.trigyn.jws.dbutils.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;

public interface IModuleListingRepository extends JpaRepositoryImplementation<ModuleListing, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_BY_MODULE_ID)
	ModuleDetailsVO getModuleDetails(@Param("moduleId") String moduleId, @Param("languageId") Integer languageId, 
			@Param("defaultLanguageId") Integer defaultLanguageId);

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_PARENT_MODULES)
	List<ModuleDetailsVO> getAllParentModules(@Param("isNotHomePage") Integer isNotHomePage, @Param("languageId") Integer languageId, 
			@Param("defaultLanguageId") Integer defaultLanguageId, @Param("isInsideMenu") Integer isInsideMenu);

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULES_DETAILS)
	List<ModuleDetailsVO> getAllModulesDetails(@Param("languageId") Integer languageId, @Param("defaultLanguageId") Integer defaultLanguageId);

	@Query(QueryStore.JPA_QUERY_TO_GET_ROLE_SPECIFIC_MENU_MODULES_DETAILS)
	List<ModuleDetailsVO> getRoleSpecificModulesDetails(@Param("languageId") Integer languageId, @Param("defaultLanguageId") Integer defaultLanguageId, 
			@Param("roleList") List<String> roleList, @Param("jeraModuleID") String jeraModuleID);

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_NAME)
	String getModuleIdByName(@Param("moduleName") String moduleName, @Param("languageId") Integer languageId, 
			@Param("defaultLanguageId") Integer defaultLanguageId);

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_SEQUENCE)
	String getModuleIdBySequence(@Param("sequence") Integer sequence);

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_PARENT_SEQUENCE)
	String getParentModuleIdBySequence(@Param("parentModuleId") String parentModuleId, @Param("sequence") Integer sequence);

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULE_ID)
	List<ModuleDetailsVO> getAllModuleId(@Param("moduleId") String moduleId);

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_ID_BY_URL)
	List<ModuleDetailsVO> getModuleIdByURL(@Param("moduleURL") String moduleURL, @Param("moduleId") String moduleId);

	@Query(QueryStore.JPA_QUERY_TO_GET_TARGET_TYPE_BY_URL)
	ModuleDetailsVO getTargetTypeByURL(@Param("moduleURL") String moduleURL);

	@Query(QueryStore.JPA_QUERY_TO_GET_TARGET_TYPE_FOR_URL)
	List<ModuleDetailsVO> getTargetTypeURL(@Param("moduleURL") String moduleURL);

	@Query(" FROM ModuleListing WHERE moduleId=:moduleId")
	ModuleListing getModuleListing(@Param("moduleId") String moduleId);

	@Query("FROM ModuleListing ml WHERE moduleId = :moduleId AND moduleId NOT IN(SELECT mra.moduleId FROM ModuleRoleAssociation AS mra WHERE mra.moduleId = :moduleId AND mra.isDeleted = 0)")
	ModuleListing getModuleListingByRole(@Param("moduleId") String moduleId);

	@Query(QueryStore.JPA_QUERY_TO_GET_HOME_PAGE_URL_BY_ROLE_IDS)
	List<String> getModuleURLByRoleId(@Param("roleIdList") List<String> roleIdList, @Param("isHomePage") Integer isHomePage);

	@Query(QueryStore.JPA_QUERY_TO_GET_IS_HOME_PAGE_BY_URL)
	Integer getIsHomePageByUrl(@Param("targetName") String targetName);

	@Query(QueryStore.JPA_QUERY_TO_GET_ROUTER_SYSTEM_URL)
	List<String> getModuleURLBySystem();

	@Query(QueryStore.JPA_QUERY_TO_GET_CHECK_IF_FORM_IO)
	Integer isFormIO(@Param("moduleUrl") String moduleUrl);
}
