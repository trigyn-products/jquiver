package com.trigyn.jws.webstarter.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class ImportExportCrudDAO extends DBConnection {

	private final static Logger logger = LogManager.getLogger(ImportExportCrudDAO.class);

	@Autowired
	public ImportExportCrudDAO(DataSource dataSource) {
		super(dataSource);
	}

	public List<Object> getAllExportableData(String querySQL, List<String> includeSystemConfigList,
			Integer systemConfigType, List<String> excludeCustomConfigList, Integer customConfigType) throws Exception {

		Query query = getCurrentSession().createQuery(querySQL);
		if (excludeCustomConfigList != null)
			query.setParameterList("excludeCustomConfigList", excludeCustomConfigList);
		if (customConfigType != null)
			query.setParameter("customConfigType", customConfigType);
		if (includeSystemConfigList != null)
			query.setParameterList("includeSystemConfigList", includeSystemConfigList);
		if (systemConfigType != null)
			query.setParameter("systemConfigType", systemConfigType);

		return query.list();
	}

	public List<Object> getExportableDataWithIntegerList(String querySQL, List<Integer> includeSystemConfigList,
			Integer systemConfigType, List<Integer> excludeCustomConfigList, Integer customConfigType)
			throws Exception {

		Query query = getCurrentSession().createQuery(querySQL);
		if (excludeCustomConfigList != null)
			query.setParameterList("excludeCustomConfigList", excludeCustomConfigList);
		if (customConfigType != null)
			query.setParameter("customConfigType", customConfigType);
		if (includeSystemConfigList != null)
			query.setParameterList("includeSystemConfigList", includeSystemConfigList);
		if (systemConfigType != null)
			query.setParameter("systemConfigType", systemConfigType);

		return query.list();
	}

	public List<Object> getRBExportableData(String querySQL, List<String> includeSystemConfigList,
			List<String> excludeCustomConfigList) throws Exception {

		Query query = getCurrentSession().createQuery(querySQL);
		if (excludeCustomConfigList != null)
			query.setParameterList("excludeCustomConfigList", excludeCustomConfigList);
		query.setParameter("customConfigType", "jws.%");
		if (includeSystemConfigList != null)
			query.setParameterList("includeSystemConfigList", includeSystemConfigList);
		query.setParameter("systemConfigType", "jws.%");

		return query.list();
	}

	public List<Map<String, Object>> getAllCustomEntity() {
		String querySQL = "SELECT gd.grid_id as id, gd.grid_name as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'Grid' as enityType"
				+ " FROM grid_details gd LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = gd.grid_id "
				+ " WHERE gd.grid_type_id = 1 GROUP BY gd.grid_id" + " UNION "
				+ " SELECT tm.template_id as id, tm.template_name as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'Templates' as enityType"
				+ " FROM template_master tm LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = tm.template_id WHERE tm.template_type_id = 1 GROUP BY tm.template_id"
				+ " UNION "
				+ " SELECT rb.resource_key as id, rb.`text` as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'ResourceBundle' as enityType"
				+ " FROM resource_bundle rb LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = rb.resource_key "
				+ " WHERE rb.resource_key NOT LIKE 'jws.%' GROUP BY rb.resource_key" + " UNION "
				+ " SELECT au.ac_id as id, au.ac_description as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'Autocomplete' as enityType"
				+ " FROM autocomplete_details au LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = au.ac_id WHERE au.ac_type_id = 1 GROUP BY au.ac_id"
				+ " UNION "
				+ " SELECT gun.notification_id as id, gun.message_text as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'Notification' as enityType"
				+ " FROM generic_user_notification gun LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = gun.notification_id GROUP BY gun.notification_id"
				+ " UNION "
				+ " SELECT db.dashboard_id as id, db.dashboard_name as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'Dashboard' as enityType"
				+ " FROM dashboard db LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = db.dashboard_id WHERE db.dashboard_type = 1 GROUP BY db.dashboard_id"
				+ " UNION "
				+ " SELECT dl.dashlet_id as id, dl.dashlet_name as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'Dashlets' as enityType"
				+ " FROM dashlet dl LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = dl.dashlet_id WHERE dl.dashlet_type_id = 1 GROUP BY dl.dashlet_id"
				+ " UNION "
				+ " SELECT df.form_id as id, df.form_description as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'DynamicForm' as enityType"
				+ " FROM dynamic_form df LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = df.form_id WHERE df.form_type_id = 1 GROUP BY df.form_id"
				+ " UNION "
				+ " SELECT fu.file_upload_config_id as id, fu.file_type_supported as name, 'NA' as versionID, 'FileManager' as enityType"
				+ " FROM file_upload_config fu LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = fu.file_upload_config_id GROUP BY fu.file_upload_config_id"
				+ " UNION "
				+ " SELECT dr.jws_dynamic_rest_id as id, dr.jws_dynamic_rest_url as name, IF(mv.version_id>=1, MAX(mv.version_id), '1.0') as versionID, 'DynaRest' as enityType"
				+ " FROM jws_dynamic_rest_details dr LEFT OUTER JOIN jws_module_version AS mv ON mv.entity_id = dr.jws_dynamic_rest_id "
				+ " WHERE dr.jws_dynamic_rest_type_id = 1 GROUP BY dr.jws_dynamic_rest_id" + "";

		return getJdbcTemplate().queryForList(querySQL);
	}

	public List<Map<String, Object>> getCustomEntityCount() {
		String querySQL = "SELECT count(*) as count, 'Grid' as enityType"
				+ " FROM grid_details gd WHERE gd.grid_type_id = 1" + " UNION "
				+ " SELECT count(*) as count, 'Templates' as enityType"
				+ " FROM template_master tm WHERE tm.template_type_id = 1" + " UNION "
				+ " SELECT count(*) as count, 'ResourceBundle' as enityType"
				+ " FROM resource_bundle rb WHERE rb.resource_key NOT LIKE 'jws.%'" + " UNION "
				+ " SELECT count(*) as count, 'Autocomplete' as enityType"
				+ " FROM autocomplete_details au WHERE au.ac_type_id = 1" + " UNION "
				+ " SELECT count(*) as count, 'Notification' as enityType" + " FROM generic_user_notification gun"
				+ " UNION " + " SELECT count(*) as count, 'Dashboard' as enityType"
				+ " FROM dashboard db WHERE db.dashboard_type = 1" + " UNION "
				+ " SELECT count(*) as count, 'Dashlets' as enityType" + " FROM dashlet dl WHERE dl.dashlet_type_id = 1"
				+ " UNION " + " SELECT count(*) as count, 'DynamicForm' as enityType"
				+ " FROM dynamic_form df WHERE df.form_type_id = 1" + " UNION "
				+ " SELECT count(*) as count, 'FileManager' as enityType" + " FROM file_upload_config fu" + " UNION "
				+ " SELECT count(*) as count, 'DynaRest' as enityType"
				+ " FROM jws_dynamic_rest_details dr WHERE dr.jws_dynamic_rest_type_id = 1 " + " UNION "
				+ " SELECT 0 as count, 'Permission' as enityType" + " FROM jws_role jr" + " UNION "
				+ " SELECT 0 as count, 'SiteLayout' as enityType" + " FROM module_listing ml" + " UNION "
				+ " SELECT 0 as count, 'ApplicationConfiguration' as enityType" + " FROM jws_property_master"
				+ " UNION " + " SELECT 0 as count, 'ManageUsers' as enityType" + " FROM jws_user" + " UNION "
				+ " SELECT 0 as count, 'ManageRoles' as enityType" + " FROM jws_role" + "";

		return getJdbcTemplate().queryForList(querySQL);
	}

	public List<Map<String, Object>> getAllEntityCount() {
		String querySQL = "SELECT count(*) as totalCount, 'Grid' as enityType" + " FROM grid_details" + " UNION "
				+ " SELECT count(*) as totalCount, 'Templates' as enityType" + " FROM template_master" + " UNION "
				+ " SELECT count(*) as totalCount, 'ResourceBundle' as enityType" + " FROM resource_bundle" + " UNION "
				+ " SELECT count(*) as totalCount, 'Autocomplete' as enityType" + " FROM autocomplete_details"
				+ " UNION " + " SELECT count(*) as totalCount, 'Notification' as enityType"
				+ " FROM generic_user_notification gun" + " UNION "
				+ " SELECT count(*) as totalCount, 'Dashboard' as enityType" + " FROM dashboard" + " UNION "
				+ " SELECT count(*) as totalCount, 'Dashlets' as enityType" + " FROM dashlet" + " UNION "
				+ " SELECT count(*) as totalCount, 'DynamicForm' as enityType" + " FROM dynamic_form" + " UNION "
				+ " SELECT count(*) as totalCount, 'FileManager' as enityType" + " FROM file_upload_config" + " UNION "
				+ " SELECT count(*) as totalCount, 'DynaRest' as enityType" + " FROM jws_dynamic_rest_details"
				+ " UNION " + " SELECT count(*) as totalCount, 'Permission' as enityType"
				+ " FROM (SELECT jera.entity_role_id AS entityRoleId "
				+ " FROM jws_entity_role_association AS jera LEFT OUTER JOIN jws_role AS jr ON jr.role_id = jera.role_id AND jr.is_active = 1 "
				+ " LEFT OUTER JOIN jws_master_modules jmm ON jmm.module_id = jera.module_id "
				+ " WHERE jera.is_active = 1 AND jera.module_type_id = 0) tableName " + " UNION "
				+ " SELECT count(*) as totalCount, 'SiteLayout' as enityType" + " FROM module_listing ml" + " UNION "
				+ " SELECT count(*) as totalCount, 'ApplicationConfiguration' as enityType"
				+ " FROM jws_property_master" + " UNION " + " SELECT count(*) as totalCount, 'ManageUsers' as enityType"
				+ " FROM jws_user" + " UNION " + " SELECT count(*) as totalCount, 'ManageRoles' as enityType"
				+ " FROM jws_role" + "";

		return getJdbcTemplate().queryForList(querySQL);
	}

}
