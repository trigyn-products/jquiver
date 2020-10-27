package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.usermanagement.utils.Constants;

@Repository
@Transactional
public class AuthorizedValidatorDAO extends DBConnection {

	
	@Autowired
	public AuthorizedValidatorDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	
	public Long hasAccessToCurrentDynamicForm(String formId,List<String> roleNames) {
		
		Query query = getCurrentSession().createQuery(
		        " SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN DynamicForm df ON jera.entityId = df.formId " + 
		        " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)" + 
		        " AND  df.formId=:formId ");
		query.setParameter("formId", formId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long)query.uniqueResult();
		return count;
		
	}


	public Long hasAccessToGridUtils(String gridId, List<String> roleNames) {
		
		Query query = getCurrentSession().createQuery(
		        " SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN GridDetails gd ON jera.entityId = gd.gridId " + 
		        " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)" + 
		        " AND  gd.gridId=:gridId ");
		query.setParameter("gridId", gridId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long)query.uniqueResult();
		return count;
		
	}
	
	public Long hasAccessToDashboard(String dashboardId, List<String> roleNames) {
		
		Query query = getCurrentSession().createQuery(
		        " SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN Dashboard db ON jera.entityId = db.dashboardId " + 
		        " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)" + 
		        " AND  db.dashboardId=:dashboardId ");
		query.setParameter("dashboardId", dashboardId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long)query.uniqueResult();
		return count;
		
	}


	public Long hasAccessToDynamicRest(String requestUri,String requestMethod, List<String> roleNames) {
		Query query = getCurrentSession().createQuery(
		        " SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN JwsDynamicRestDetail jdrd ON jera.entityId = jdrd.jwsDynamicRestId " + 
		        " INNER JOIN jdrd.jwsRequestTypeDetail jrtd ON jdrd.jwsDynamicRestTypeId = jrtd.jwsRequestTypeDetailsId INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)" + 
		        " AND  jdrd.jwsDynamicRestUrl=:requestUri AND  jrtd.jwsRequestType=:requestMethod");
		query.setParameter("requestUri", requestUri);
		query.setParameter("requestMethod", requestMethod);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long)query.uniqueResult();
		return count;
	}


	public Long hasAccessToTemplate(String templateName, List<String> roleNames) {
		Query query = getCurrentSession().createQuery(
		        " SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN TemplateMaster tm ON jera.entityId = tm.templateId " + 
		        " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)" + 
		        " AND  tm.templateName=:templateName ");
		query.setParameter("templateName", templateName);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long)query.uniqueResult();
		return count;
	}


	public Long hasAccessToAutocomplete(String autocompleteId, List<String> roleNames) {
		Query query = getCurrentSession().createQuery(
		        " SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN Autocomplete ac ON jera.entityId = ac.autocompleteId " + 
		        " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)" + 
		        " AND  ac.autocompleteId=:autocompleteId ");
		query.setParameter("autocompleteId", autocompleteId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long)query.uniqueResult();
		return count;
	}


	public Long hasAccessToSiteLayout(String moduleUrl, List<String> roleNames) {
		Query query = getCurrentSession().createQuery(
		        " SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN ModuleListing ml ON jera.entityId= ml.moduleId " + 
		        " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)" + 
		        " AND  ml.moduleUrl=:moduleUrl ");
		query.setParameter("moduleUrl", moduleUrl);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long)query.uniqueResult();
		return count;
	}
}
