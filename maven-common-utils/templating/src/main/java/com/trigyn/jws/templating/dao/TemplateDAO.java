package com.trigyn.jws.templating.dao;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.vo.TemplateVO;

@Repository
public class TemplateDAO extends DBConnection {

    @Autowired
    public TemplateDAO(DataSource dataSource) {
        super(dataSource);
    }

    public Map<String, Object> getVelocityDataById(String vmMasterId) throws Exception {
        Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_TO_GET_BY_ID);
        query.setParameter("vmMasterId", vmMasterId);
        Map<String, Object> resultData = (Map<String, Object>) query.uniqueResult();
        return resultData;
    }

    public String checkVelocityData(String velocityName) throws Exception {
        Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_TO_CHECK_NAME);
        query.setParameter("vmName", velocityName);
        String data = (String) query.uniqueResult();
        return data;
    }

    public void saveVelocityTemplateData(TemplateMaster templateDetails) throws Exception {
        getCurrentSession().saveOrUpdate(templateDetails);
    }


	public void updateChecksum(TemplateVO templateVO) {
		Query query = getCurrentSession().createQuery("UPDATE TemplateMaster SET checksum=:checksum WHERE templateId=:templateId");
		 query.setParameter("checksum", templateVO.getChecksum());
		 query.setParameter("templateId", templateVO.getTemplateId());
		 query.executeUpdate();
	}
	
	  public TemplateMaster getTemplateDetailsByName(String templateName) {
	        Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_TO_GET_VMTEMPLATE_BY_VMNAME);
	        query.setParameter("templateName", templateName);
	        TemplateMaster template = (TemplateMaster) query.uniqueResult();
	        return template;
	    }

}