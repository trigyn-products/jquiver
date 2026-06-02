package com.trigyn.jws.usermanagement.repository;

import java.util.regex.Pattern;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.utils.AccessControlCacheUtil;
import com.trigyn.jws.webstarter.utils.RedissonQueryCacheManagerUtil;
import com.trigyn.jws.webstarter.vo.AccessEntityType;

@Repository
@Transactional
public class AuthorizedValidatorDAO extends DBConnection {
	// private static final Logger logger =
	// LogManager.getLogger(AuthorizedValidatorDAO.class);

	@Autowired
	private RedissonQueryCacheManagerUtil	cacheManager			= null;

	@Autowired
	private AccessControlCacheUtil			accessControlCacheUtil	= null;

	@Value("${jquiver.redis.cache.ttl:1800}") // 30 minutes
	private int								defaultTTLMinutes;

	public AuthorizedValidatorDAO(DataSource dataSource) {
		super(dataSource);
	}

	public Long hasAccessToCurrentDynamicForm(String formId, List<String> roleNames) {
		String cacheName = "dynamicform-access";
		return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.DYNAMIC_FORM, formId, roleNames, () -> {
			Query<Long> query = getCurrentSession().createQuery(
					" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN DynamicForm df ON jera.entityId = df.formId "
							+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
							+ " AND  df.formId=:formId ",
					Long.class);
			query.setParameter("formId", formId);
			query.setParameter("isActive", Constants.ISACTIVE);
			query.setParameterList("roleNames", roleNames);
			return query.uniqueResult();
		}, defaultTTLMinutes);
	}

	public Long hasAccessToGridUtils(String gridId, List<String> roleNames, String moduleId) {
		String cacheName = "gridUtil-access";
		return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.GRID, gridId + ":" + moduleId, roleNames,
				() -> {
					Query<Long> query = getCurrentSession().createQuery(
							" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN GridDetails gd ON jera.entityId = gd.gridId "
									+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
									+ " AND  gd.gridId=:gridId AND jera.moduleId=:moduleId ",
							Long.class);
					query.setParameter("gridId", gridId);
					query.setParameter("isActive", Constants.ISACTIVE);
					query.setParameterList("roleNames", roleNames);
					query.setParameter("moduleId", moduleId);
					return query.uniqueResult();
				}, defaultTTLMinutes);
	}

	public Long hasAccessToDashboard(String dashboardId, List<String> roleNames) {

		String cacheName = "dashboard-access";
		return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.DASHBOARD, dashboardId, roleNames, () -> {
			Query<Long> query = getCurrentSession().createQuery(
					" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN Dashboard db ON jera.entityId = db.dashboardId "
							+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
							+ " AND  db.dashboardId=:dashboardId ",
					Long.class);
			query.setParameter("dashboardId", dashboardId);
			query.setParameter("isActive", Constants.ISACTIVE);
			query.setParameterList("roleNames", roleNames);
			return query.uniqueResult();
		}, defaultTTLMinutes);
	}

	public Long hasAccessToDashlet(String dashletId, List<String> roleNames) {

		String cacheName = "dashlet-access";
		return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.DASHLET, dashletId, roleNames, () -> {
			Query<Long> query = getCurrentSession().createQuery(
					" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN Dashlet db ON jera.entityId = db.dashletId "
							+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
							+ " AND  db.dashletId=:dashletId ",
					Long.class);
			query.setParameter("dashletId", dashletId);
			query.setParameter("isActive", Constants.ISACTIVE);
			query.setParameterList("roleNames", roleNames);
			return query.uniqueResult();
		}, defaultTTLMinutes);
	}

	
public Long hasAccessToDynamicRest(String requestUri, String requestMethod, List<String> roleNames) {

	String		entityId	= requestMethod + ":" + requestUri;
	String		cacheName	= "dynamic-rest-access";

	return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.DYNAMIC_REST, entityId, roleNames, () -> {

		Query<String> restUrlQuery = getCurrentSession()
				.createQuery("SELECT DISTINCT jdrd.jwsDynamicRestUrl FROM JwsEntityRoleAssociation jera "
						+ "INNER JOIN JwsDynamicRestDetail jdrd ON jera.entityId = jdrd.jwsDynamicRestId "
						+ "INNER JOIN JwsRole jr ON jera.roleId = jr.roleId "
						+ "WHERE jera.isActive = :isActive AND jr.roleName IN (:roleNames)", String.class);
		restUrlQuery.setParameter("isActive", Constants.ISACTIVE);
		restUrlQuery.setParameterList("roleNames", roleNames);

		List<String> dynamicUrls = restUrlQuery.getResultList();

		for (String dynamicUrl : dynamicUrls) {
			String regex = convertToRegex(dynamicUrl);
			if (requestUri.matches(regex)) {
				Query<Long> query = getCurrentSession()
						.createQuery("SELECT COUNT(*) FROM JwsEntityRoleAssociation jera "
								+ "INNER JOIN JwsDynamicRestDetail jdrd ON jera.entityId = jdrd.jwsDynamicRestId "
								+ "INNER JOIN JwsRole jr ON jera.roleId = jr.roleId "
								+ "WHERE jera.isActive = :isActive " + "AND jr.roleName IN (:roleNames) "
								+ "AND jdrd.jwsDynamicRestUrl = :requestUri", Long.class);
				query.setParameter("requestUri", dynamicUrl);
				query.setParameter("isActive", Constants.ISACTIVE);
				query.setParameterList("roleNames", roleNames);

				Long count = query.uniqueResult();
				if (count != null && count > 0) {
					return count;
				}
			}
		}
		return 0L;

	}, defaultTTLMinutes);
}

	private String convertToRegex(String dynamicUrl) {
		if (dynamicUrl == null || dynamicUrl.isBlank())
			return "";

		if (dynamicUrl.endsWith("/") && !dynamicUrl.equals("/")) {
			dynamicUrl = dynamicUrl.substring(0, dynamicUrl.length() - 1);
		}

		String regex;

		if (dynamicUrl.endsWith("/**")) {
			regex = dynamicUrl.replace("/**", "");
			regex = regex.replace(".", "\\\\.") + "(?:/.*)?";
		} else if (dynamicUrl.endsWith("/*")) {
			regex = dynamicUrl.replace("/*", "");
			regex = regex.replace(".", "\\\\.") + "/[^/]+";
		} else {
			regex = dynamicUrl.replace(".", "\\\\.");
		}

		return "^" + regex + "$";
	}

	public Long hasAccessToTemplate(String templateName, List<String> roleNames) {
		String cacheName = "template-access";
		return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.TEMPLATE, templateName, roleNames, () -> {
			Query<Long> query = getCurrentSession().createQuery(
					" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN TemplateMaster tm ON jera.entityId = tm.templateId "
							+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
							+ " AND  tm.templateName=:templateName ",
					Long.class);
			query.setParameter("templateName", templateName);
			query.setParameter("isActive", Constants.ISACTIVE);
			query.setParameterList("roleNames", roleNames);
			Long result = query.uniqueResult();
			// logger.debug("Query returned: {}", result);
			return result;
		}, defaultTTLMinutes);
	}

	public Long hasAccessToAutocomplete(String autocompleteId, List<String> roleNames, String moduleId) {
		String cacheName = "autocomplete-access";
		return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.TEMPLATE, autocompleteId + ":" + moduleId,
				roleNames, () -> {
					Query<Long> query = getCurrentSession().createQuery(
							" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN Autocomplete ac ON jera.entityId = ac.autocompleteId "
									+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
									+ " AND  ac.autocompleteId=:autocompleteId AND jera.moduleId=:moduleId ",
							Long.class);
					query.setParameter("autocompleteId", autocompleteId);
					query.setParameter("isActive", Constants.ISACTIVE);
					query.setParameterList("roleNames", roleNames);
					query.setParameter("moduleId", moduleId);
					return query.uniqueResult();
				}, defaultTTLMinutes);
	}

	public Long hasAccessToSiteLayout(String moduleUrl, List<String> roleNames) {
		String cacheName = "siteLayout-access";
		return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.SITE_LAYOUT, moduleUrl, roleNames, () -> {
			Query<Long> query = getCurrentSession().createQuery(
					" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN ModuleListing ml ON jera.entityId= ml.moduleId "
							+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive=:isActive AND jr.roleName IN(:roleNames)"
							+ " AND  ml.moduleUrl = :moduleUrl ",
					Long.class);
			query.setParameter("moduleUrl", moduleUrl);
			query.setParameter("isActive", Constants.ISACTIVE);
			query.setParameterList("roleNames", roleNames);
			return query.uniqueResult();
		}, defaultTTLMinutes);
	}

	public Long hasAccessToEntity(String moduleName, String entityId, List<String> roleNames) {

		String	cacheName		= "entity-access";
		String	compositeKey	= moduleName + ":" + entityId;
		return accessControlCacheUtil.hasAccess(cacheName, AccessEntityType.ENTITY, compositeKey, roleNames, () -> {
			Query<Long> query = getCurrentSession().createQuery(
					" SELECT COUNT (*) from JwsEntityRoleAssociation jera INNER JOIN JwsMasterModules AS jmm ON jmm.moduleId = jera.moduleId AND jmm.moduleName = :moduleName"
							+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId  WHERE  jera.isActive = :isActive AND jr.roleName IN (:roleNames)"
							+ " AND  jera.entityId =:entityId ",
					Long.class);
			query.setParameter("moduleName", moduleName);
			query.setParameterList("roleNames", roleNames);
			query.setParameter("isActive", Constants.ISACTIVE);
			query.setParameter("entityId", entityId);
			return query.uniqueResult();
		}, defaultTTLMinutes);

	}

	// --- NAME FETCH METHODS (cached) ---

	public String getDashboardNameById(String dashboardId) {
		return fetchStringById("dashboard-name-cache", "getDashboardNameById:" + dashboardId,
				"SELECT db.dashboardName FROM Dashboard db WHERE db.dashboardId = :dashboardId", "dashboardId",
				dashboardId);
	}

	public String getDashletNameById(String dashletId) {
		return fetchStringById("dashlet-name-cache", "getDashletNameById:" + dashletId,
				"SELECT db.dashletName FROM Dashlet db WHERE db.dashletId = :dashletId", "dashletId", dashletId);
	}

	public String getDynamicFormNameById(String dynamicFormId) {
		return fetchStringById("form-name-cache", "getDynamicFormNameById:" + dynamicFormId,
				"SELECT df.formName FROM DynamicForm df WHERE df.formId = :dynamicFormId", "dynamicFormId",
				dynamicFormId);
	}

	public String getFileBinIdByFileUploadId(String fileUploadId) {
		return fetchStringById("file-bin-cache", "getFileBinIdByFileUploadId:" + fileUploadId,
				"SELECT fu.fileBinId FROM FileUpload fu WHERE fu.fileUploadId = :fileUploadId", "fileUploadId",
				fileUploadId);
	}

	public String getGridNameByGridId(String gridId) {
		return fetchStringById("grid-name-cache", "getGridNameByGridId:" + gridId,
				"SELECT gd.gridName FROM GridDetails gd WHERE gd.gridId = :gridId", "gridId", gridId);
	}

	public String getManualNameById(String manualId) {
		return fetchStringById("manual-name-cache", "getManualNameById:" + manualId,
				" SELECT mt.name FROM ManualType mt WHERE mt.manualId = :manualId ", "manualId", manualId);
	}

	public String getSiteLayoutModuleNameByUrl(String moduleUrl) {
		return fetchStringById("site-layout-module-cache", "getSiteLayoutModuleNameByUrl:" + moduleUrl,
				"SELECT mlI18n.moduleName FROM ModuleListing ml INNER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = 1 "
						+ "WHERE ml.moduleUrl LIKE :moduleUrl",
				"moduleUrl", moduleUrl);
	}

	// --- COMMON UTILITY ---

	private String fetchStringById(String cacheName, String cacheKey, String hql, String paramName, String paramValue) {
		return cacheManager.fetchJpaDto(cacheName, cacheKey, () -> {
			Query<String> query = getCurrentSession().createQuery(hql, String.class);
			query.setParameter(paramName, paramValue);
			return query.uniqueResult();
		}, defaultTTLMinutes);
	}
}
