package app.trigyn.common.resourcebundle.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.trigyn.common.resourcebundle.dao.QueryStore;
import app.trigyn.common.resourcebundle.entities.ResourceBundle;
import app.trigyn.common.resourcebundle.entities.ResourceBundlePK;
import app.trigyn.common.resourcebundle.vo.ResourceBundleVO;

@Repository
public interface IResourceBundleRepository extends JpaRepository<ResourceBundle, ResourceBundlePK> {

	@Query(QueryStore.JPA_QUERY_TO_GET_MESSAGE_DETAILS_BY_RESOURCE_KEY)
	List<ResourceBundleVO> findResourceBundleByKey(String resourceBundleKey);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_MESSAGE_BY_LANGUAGE_CODE)
	String findByKeyAndLanguageCode(String resourceBundleKey, String localeCode, String defaultLocaleCode, Integer isDeleted);
	
	@Query(QueryStore.JPA_QUERY_TO_CHECK_RESOURCE_KEY_EXIST)
	String checkResourceKeyExist(String resourceBundleKey);
}
