package app.trigyn.common.resourcebundle.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.trigyn.common.resourcebundle.dao.QueryStore;
import app.trigyn.common.resourcebundle.entities.Language;
import app.trigyn.common.resourcebundle.vo.LanguageVO;

@Repository
public interface ILanguageRepository extends JpaRepository<Language, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_LANGUAGES)
	List<LanguageVO> getAllLanguages(Integer isDeleted);
}
