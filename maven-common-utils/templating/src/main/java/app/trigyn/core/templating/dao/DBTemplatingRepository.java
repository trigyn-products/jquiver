package app.trigyn.core.templating.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import app.trigyn.core.templating.entities.TemplateMaster;
import app.trigyn.core.templating.vo.TemplateVO;

@Repository
public interface DBTemplatingRepository extends JpaRepositoryImplementation<TemplateMaster, String>{
    
    @Query(QueryStore.JPA_QUERY_TO_GET_TEMPALTE_DETAILS)
    TemplateVO findByVmName(String vmName);
}