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

	public Long hasAccessToCurrentDynamicForm(String formId, List<String> roleNames) {

		Query query = getCurrentSession()
				.createQuery(" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN DynamicForm df ON jera.entityId = df.formId "
						+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
						+ " AND  df.formId=:formId ");
		query.setParameter("formId", formId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	public Long hasAccessToGridUtils(String gridId, List<String> roleNames, String moduleId) {

		Query query = getCurrentSession()
				.createQuery(" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN GridDetails gd ON jera.entityId = gd.gridId "
						+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
						+ " AND  gd.gridId=:gridId AND jera.moduleId=:moduleId ");
		query.setParameter("gridId", gridId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		query.setParameter("moduleId", moduleId);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	public Long hasAccessToDashboard(String dashboardId, List<String> roleNames) {

		Query query = getCurrentSession().createQuery(
				" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN Dashboard db ON jera.entityId = db.dashboardId "
						+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
						+ " AND  db.dashboardId=:dashboardId ");
		query.setParameter("dashboardId", dashboardId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long) query.uniqueResult();
		return count;
	}
	
	public Long hasAccessToDashlet(String dashletId, List<String> roleNames) {

		Query query = getCurrentSession().createQuery(
				" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN Dashlet db ON jera.entityId = db.dashletId "
						+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
						+ " AND  db.dashletId=:dashletId ");
		query.setParameter("dashletId", dashletId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	public Long hasAccessToDynamicRest(String requestUri, String requestMethod, List<String> roleNames) {
		Query query = getCurrentSession().createQuery(
				" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN JwsDynamicRestDetail jdrd ON jera.entityId = jdrd.jwsDynamicRestId "
						+ "  INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
						+ " AND  jdrd.jwsDynamicRestUrl=:requestUri");
		query.setParameter("requestUri", requestUri);
//		query.setParameter("requestMethod", requestMethod);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	public Long hasAccessToTemplate(String templateName, List<String> roleNames) {
		Query query = getCurrentSession().createQuery(
				" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN TemplateMaster tm ON jera.entityId = tm.templateId "
						+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
						+ " AND  tm.templateName=:templateName ");
		query.setParameter("templateName", templateName);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	public Long hasAccessToAutocomplete(String autocompleteId, List<String> roleNames, String moduleId) {
		Query query = getCurrentSession().createQuery(
				" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN Autocomplete ac ON jera.entityId = ac.autocompleteId "
						+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
						+ " AND  ac.autocompleteId=:autocompleteId AND jera.moduleId=:moduleId ");
		query.setParameter("autocompleteId", autocompleteId);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		query.setParameter("moduleId", moduleId);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	public Long hasAccessToSiteLayout(String moduleUrl, List<String> roleNames) {
		Query query = getCurrentSession().createQuery(
				" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN ModuleListing ml ON jera.entityId= ml.moduleId "
						+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
						+ " AND  ml.moduleUrl = :moduleUrl ");
		query.setParameter("moduleUrl", moduleUrl);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameterList("roleNames", roleNames);
		Long count = (Long) query.uniqueResult();
		return count;
	}

	public Long hasAccessToEntity(String moduleName, String entityId, List<String> roleNames) {

		Query query = getCurrentSession().createQuery(
				" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN JwsMasterModules AS jmm ON jmm.moduleId = jera.moduleId AND jmm.moduleName = :moduleName"
						+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive = :isActive AND jr.roleName IN (:roleNames)"
						+ " AND  jera.entityId =:entityId ");
		query.setParameter("moduleName", moduleName);
		query.setParameterList("roleNames", roleNames);
		query.setParameter("isActive", Constants.ISACTIVE);
		query.setParameter("entityId", entityId);
		Long count = (Long) query.uniqueResult();
		return count;

	}

	public String getSiteLayoutModuleNameByUrl(String moduleUrl) {
		Query query = getCurrentSession().createQuery(
				" SELECT mlI18n.moduleName FROM ModuleListing ml INNER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = 1 "
						+ " WHERE ml.moduleUrl LIKE :moduleUrl ");
		query.setParameter("moduleUrl", moduleUrl);
		String count = (String) query.uniqueResult();
		return count;
	}

	public String getGridNameByGridId(String gridId) {
		Query query = getCurrentSession().createQuery(" SELECT gd.gridName FROM GridDetails gd WHERE gd.gridId = :gridId ");
		query.setParameter("gridId", gridId);
		String count = (String) query.uniqueResult();
		return count;
	}

	public String getDynamicFormNameById(String dynamicFormId) {
		Query query = getCurrentSession().createQuery(" SELECT df.formName FROM DynamicForm df WHERE df.formId = :dynamicFormId ");
		query.setParameter("dynamicFormId", dynamicFormId);
		String count = (String) query.uniqueResult();
		return count;
	}

	public String getDashboardNameById(String dashboarId) {
		Query query = getCurrentSession().createQuery(" SELECT db.dashboardName FROM Dashboard db WHERE db.dashboardId = :dashboarId ");
		query.setParameter("dashboarId", dashboarId);
		String count = (String) query.uniqueResult();
		return count;
	}
	
	public String getDashletNameById(String dashletId) {
		Query query = getCurrentSession().createQuery(" SELECT db.dashletName FROM Dashlet db WHERE db.dashletId = :dashletId ");
		query.setParameter("dashletId", dashletId);
		String count = (String) query.uniqueResult();
		return count;
	}

	public String getFileBinIdByFileUploadId(String fileUploadId) {
		Query query = getCurrentSession().createQuery(" SELECT fu.fileBinId FROM FileUpload fu WHERE fu.fileUploadId = :fileUploadId ");
		query.setParameter("fileUploadId", fileUploadId);
		String count = (String) query.uniqueResult();
		return count;
	}

	public String getManualNameById(String manualId) {
		Query query = getCurrentSession().createQuery(" SELECT mt.name FROM ManualType mt WHERE mt.manualId = :manualId ");
		query.setParameter("manualId", manualId);
		String count = (String) query.uniqueResult();
		return count;
	}

}
