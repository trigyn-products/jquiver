package com.trigyn.jws.resourcebundle.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.resourcebundle.dao.QueryStore;
import com.trigyn.jws.resourcebundle.entities.Language;
import com.trigyn.jws.resourcebundle.vo.LanguageVO;

@Repository
public interface ILanguageRepository extends JpaRepository<Language, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_LANGUAGES)
	List<LanguageVO> getAllLanguages(Integer isDeleted);
}
