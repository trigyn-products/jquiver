package com.trigyn.jws.resourcebundle.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.resourcebundle.entities.ResourceBundlePK;
import com.trigyn.jws.resourcebundle.utils.Constant;

@Repository
public class ResourceBundleDAO extends DBConnection {

	@Autowired
	public ResourceBundleDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void addResourceBundle(String resourceBundleKey, Integer languageId, String text) throws Exception {
		NativeQuery query = getCurrentSession().createSQLQuery(QueryStore.SQL_QUERY_TO_INSERT_RESOURCE_BUNDLE);
		query.setParameter("resourceBundleKey", resourceBundleKey);
		query.setParameter("languageId", languageId);
		query.setParameter("text", text);
		query.setParameter("isCustomUpdated", 1);
		query.executeUpdate();
	}

	public final static String QUERY_TO_GET_I18N_DATA_FOR_KEY = "SELECT rb.resource_key AS id, COALESCE( (SELECT rb1.text FROM jq_resource_bundle    AS rb1 "
			+ "JOIN jq_language AS lang ON  lang.language_id = rb1.language_id AND lang.language_code LIKE :localeId  "
			+ "WHERE rb1.resource_key = rb.resource_key) ,  rb.text) AS value FROM jq_resource_bundle    rb "
			+ "LEFT OUTER JOIN jq_language ON rb.language_id = jq_language.language_id "
			+ "WHERE jq_language.language_code LIKE :defaultLocaleId  " + "AND ( rb.resource_key IN (:keyInitials))";

	private final static String	QUERY_TO_GET_RB_ID		= " SELECT rb.resource_key FROM jq_resource_bundle rb WHERE rb.resource_key=:resourceKey"
			+ " AND rb.language_id=:languageId";

	public Object getResourceBundleData(String localeId, List<String> keyInitials) throws Exception {

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("localeId", localeId + "%");
		namedParameters.put("defaultLocaleId", Constant.DEFAULT_LOCALE);
		namedParameters.put("keyInitials", keyInitials);
		List<Map<String, Object>>	resultSet	= namedParameterJdbcTemplate.queryForList(QUERY_TO_GET_I18N_DATA_FOR_KEY, namedParameters);

		Map<String, String>			result		= new HashMap<String, String>();

		for (Map<String, Object> resourceBundleData : resultSet) {
			if (localeId.equals(Constant.DEFAULT_LOCALE)) {
				if (keyInitials.size() == 1) {
					return resourceBundleData.get("value").toString();
				} else {
					result.put(resourceBundleData.get("id").toString(), resourceBundleData.get("value").toString());
				}
			} else {
				String	originalString	= StringEscapeUtils.unescapeHtml4(resourceBundleData.get("value").toString());
				String	convertedTOUTF8	= new String(originalString.getBytes("UTF8"), "UTF8");
				if (keyInitials.size() == 1) {
					return convertedTOUTF8;
				} else {
					result.put(resourceBundleData.get("id").toString(), convertedTOUTF8);
				}
			}
		}
		return result;

	}

	public ResourceBundle findResourceBundle(ResourceBundlePK resourceBundlePK) {
		ResourceBundle resourceBundle =  hibernateTemplate.get(ResourceBundle.class, resourceBundlePK);
		if(resourceBundle != null) getCurrentSession().evict(resourceBundle);
		return resourceBundle;
	}

	public List<Map<String, Object>> findResourceBundleByData(String resourceKey, Integer languageId) {

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("languageId", languageId);
		namedParameters.put("resourceKey", resourceKey);
		List<Map<String, Object>>	resultSet	= namedParameterJdbcTemplate.queryForList(QUERY_TO_GET_RB_ID, namedParameters);
		
		return resultSet;		
	}

	@Transactional(readOnly = false)
	public void saveResourceBundle(ResourceBundle resourceBundle) {
		List<Map<String, Object>> rbKey = findResourceBundleByData(resourceBundle.getId().getResourceKey(), 
				resourceBundle.getLanguage().getLanguageId());
		if(findResourceBundle(resourceBundle.getId()) == null && (rbKey == null || rbKey.isEmpty())) {
			getCurrentSession().save(resourceBundle);			
		} else {
			getCurrentSession().saveOrUpdate(resourceBundle);
		}
	}


}