package com.trigyn.jws.templating.dao;

import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsUser;

@Repository
public class TemplateDAO extends DBConnection {

	public TemplateDAO(DataSource dataSource) {
		super(dataSource);
	}

	public TemplateMaster findTemplateById(String templateId) {
		TemplateMaster template =  getCurrentSession().get(TemplateMaster.class, templateId);
		if(template != null) getCurrentSession().evict(template);
		return template;
	}

	public Map<String, Object> getVelocityDataById(String vmMasterId) throws Exception {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_TO_GET_BY_ID, Map.class);
		query.setParameter("vmMasterId", vmMasterId);
		Map<String, Object> resultData = (Map<String, Object>) query.uniqueResult();
		return resultData;
	}

	public String checkVelocityData(String velocityName) throws Exception {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_TO_CHECK_NAME, String.class);
		query.setParameter("vmName", velocityName);
		String data = (String) query.uniqueResult();
		return data;
	}
	
	@Transactional(readOnly = false)
	public void saveVelocityTemplateData(TemplateMaster templateDetails) throws Exception {
		getCurrentSession().merge(templateDetails);
	}
	
	@Transactional(readOnly = false)
	public void saveTemplateData(TemplateMaster templateDetails) throws Exception {
		getCurrentSession().persist(templateDetails);
	}

	public void updateChecksum(TemplateVO templateVO) {
		MutationQuery query = getCurrentSession().createMutationQuery("UPDATE TemplateMaster SET checksum=:checksum WHERE templateId=:templateId");
		query.setParameter("checksum", templateVO.getChecksum());
		query.setParameter("templateId", templateVO.getTemplateId());
		query.executeUpdate();
	}

	public TemplateMaster getTemplateDetailsByName(String templateName) {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_TO_GET_VMTEMPLATE_BY_VMNAME, TemplateMaster.class);
		query.setParameter("templateName", templateName);
		TemplateMaster template = (TemplateMaster) query.uniqueResult();
		return template;
	}

	public Long getTemplateCount(String templateId) {
 		StringBuilder	stringBuilder	= new StringBuilder("SELECT count(*) FROM TemplateMaster AS d WHERE d.templateId = :templateId");
		Query			query			= getCurrentSession().createQuery(stringBuilder.toString(), Long.class);
		query.setParameter("templateId", templateId);
		return (Long) query.uniqueResult();
	}

}