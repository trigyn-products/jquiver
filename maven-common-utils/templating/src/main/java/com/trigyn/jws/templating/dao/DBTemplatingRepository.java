package com.trigyn.jws.templating.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.vo.TemplateVO;

@Repository
public interface DBTemplatingRepository extends JpaRepositoryImplementation<TemplateMaster, String>{
    
    @Query(QueryStore.JPA_QUERY_TO_GET_TEMPALTE_DETAILS)
    TemplateVO findByVmName(String vmName);
}