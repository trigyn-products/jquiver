package app.trigyn.core.menu.reposirtory.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import app.trigyn.core.menu.entities.ModuleListing;
import app.trigyn.core.menu.vo.ModuleDetailsVO;
import app.trigyn.core.menu.dao.QueryStore;

@Repository
public interface IModuleListingRepository extends JpaRepositoryImplementation<ModuleListing, Integer>{

	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_BY_MODULE_ID)
	ModuleDetailsVO getModuleDetails(String moduleId, Integer languageId, Integer defaultLanguageId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULES)
	List<ModuleDetailsVO> getAllModules(Integer languageId, Integer defaultLanguageId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_MODULES_DETAILS)
	List<ModuleDetailsVO> getAllModulesDetails(Integer languageId, Integer defaultLanguageId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_SEQUENCE)
	String getModuleIdBySequence(Integer sequence);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MODULE_SEQUENCE_BY_PARENT)
	String getParentModuleIdBySequence(String parentModuleId, Integer sequence);
	
}
