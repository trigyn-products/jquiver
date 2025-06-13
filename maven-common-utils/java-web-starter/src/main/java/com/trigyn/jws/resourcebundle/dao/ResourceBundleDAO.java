package com.trigyn.jws.resourcebundle.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.query.MutationQuery;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.resourcebundle.entities.ResourceBundlePK;
import com.trigyn.jws.resourcebundle.utils.Constant;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.webstarter.utils.RedissonQueryCacheManagerUtil;

import jakarta.persistence.NoResultException;

@Repository
public class ResourceBundleDAO extends DBConnection {

	public ResourceBundleDAO(DataSource dataSource) {
		super(dataSource);
	}

	@Value("${jquiver.redis.cache.enabled:false}")
	private boolean							isCacheEnabled;

	@Autowired(required = false)
	private RedissonClient					redissonClient	= null;

	@Autowired
	private RedissonQueryCacheManagerUtil	cacheManager = null;

	@Value("${cache.ttl.resourceBundle:30}")
	private long							resourceBundleTtl;

	public void addResourceBundle(String resourceBundleKey, Integer languageId, String text) throws Exception {
		MutationQuery query = getCurrentSession()
				.createNativeMutationQuery(QueryStore.SQL_QUERY_TO_INSERT_RESOURCE_BUNDLE);
		query.setParameter("resourceBundleKey", resourceBundleKey);
		query.setParameter("languageId", languageId);
		query.setParameter("text", text);
		query.setParameter("isCustomUpdated", 1);
		query.executeUpdate();
	}

	public final static String	QUERY_TO_GET_I18N_DATA_FOR_KEY	= "SELECT rb.resource_key AS id, COALESCE( (SELECT rb1.text FROM jq_resource_bundle    AS rb1 "
			+ "JOIN jq_language AS lang ON  lang.language_id = rb1.language_id AND lang.language_code LIKE :localeId  "
			+ "WHERE rb1.resource_key = rb.resource_key) ,  rb.text) AS value FROM jq_resource_bundle    rb "
			+ "LEFT OUTER JOIN jq_language ON rb.language_id = jq_language.language_id "
			+ "WHERE jq_language.language_code LIKE :defaultLocaleId  " + "AND ( rb.resource_key IN (:keyInitials))";

	private final static String	QUERY_TO_GET_RB_ID				= " SELECT rb.resource_key FROM jq_resource_bundle rb WHERE rb.resource_key=:resourceKey"
			+ " AND rb.language_id=:languageId";

	// public Object getResourceBundleData(String localeId, List<String>
	// keyInitials) throws Exception {
	//
	// Map<String, Object> namedParameters = new HashMap<String, Object>();
	// namedParameters.put("localeId", localeId + "%");
	// namedParameters.put("defaultLocaleId", Constant.DEFAULT_LOCALE);
	// namedParameters.put("keyInitials", keyInitials);
	// List<Map<String, Object>> resultSet =
	// namedParameterJdbcTemplate.queryForList(QUERY_TO_GET_I18N_DATA_FOR_KEY,
	// namedParameters);
	//
	// Map<String, String> result = new HashMap<String, String>();
	//
	// for (Map<String, Object> resourceBundleData : resultSet) {
	// if (localeId.equals(Constant.DEFAULT_LOCALE)) {
	// if (keyInitials.size() == 1) {
	// return resourceBundleData.get("value").toString();
	// } else {
	// result.put(resourceBundleData.get("id").toString(),
	// resourceBundleData.get("value").toString());
	// }
	// } else {
	// String originalString =
	// StringEscapeUtils.unescapeHtml4(resourceBundleData.get("value").toString());
	// String convertedTOUTF8 = new String(originalString.getBytes("UTF8"), "UTF8");
	// if (keyInitials.size() == 1) {
	// return convertedTOUTF8;
	// } else {
	// result.put(resourceBundleData.get("id").toString(), convertedTOUTF8);
	// }
	// }
	// }
	// return result;
	//
	// }

	public Object getResourceBundleData(String localeId, List<String> keyInitials) throws Exception {

		String cacheKey = localeId + "::" + String.join(",", keyInitials);
		// If caching is disabled or client isn’t available, skip straight to DB
		if (!isCacheEnabled || redissonClient == null) {
			return queryAndTransform(localeId, keyInitials);
		}
		// Use RMapCache to store raw resultSet
		RMapCache<String, List<Map<String, Object>>>	cache		= redissonClient
				.getMapCache("resourceBundleDataQueryCache");
		List<Map<String, Object>>						resultSet	= cache.get(cacheKey);

		if (resultSet == null) {
			// Cache miss → load from DB
			resultSet = fetchResourceBundleData(localeId, keyInitials);

			// Cache for 30 minutes
			cache.put(cacheKey, resultSet, 30, TimeUnit.MINUTES);
		}

		return transformResult(localeId, keyInitials, resultSet);

	}

	/** Always query the DB (no cache) and apply your transformation logic */
	private Object queryAndTransform(String localeId, List<String> keyInitials) throws Exception {
		List<Map<String, Object>> resultSet = fetchResourceBundleData(localeId, keyInitials);
		return transformResult(localeId, keyInitials, resultSet);
	}

	/** Raw JDBC fetch of the `List<Map<String,Object>>` */
	private List<Map<String, Object>> fetchResourceBundleData(String localeId, List<String> keyInitials) {
		Map<String, Object> params = new HashMap<>();
		params.put("localeId", localeId + "%");
		params.put("defaultLocaleId", Constant.DEFAULT_LOCALE);
		params.put("keyInitials", keyInitials);
		return namedParameterJdbcTemplate.queryForList(QUERY_TO_GET_I18N_DATA_FOR_KEY, params);
	}

	/** Convert raw JDBC result‐set into your single‐value or map return */
	private Object transformResult(String localeId, List<String> keyInitials, List<Map<String, Object>> resultSet)
			throws Exception {
		Map<String, String> result = new HashMap<String, String>();

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
		ResourceBundle resourceBundle = getCurrentSession().get(ResourceBundle.class, resourceBundlePK);
		if (resourceBundle != null)
			getCurrentSession().evict(resourceBundle);
		return resourceBundle;
	}

	public List<Map<String, Object>> findResourceBundleByData(String resourceKey, Integer languageId) {

		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("languageId", languageId);
		namedParameters.put("resourceKey", resourceKey);
		List<Map<String, Object>> resultSet = namedParameterJdbcTemplate.queryForList(QUERY_TO_GET_RB_ID,
				namedParameters);

		return resultSet;
	}

	@Transactional(readOnly = false)
	public void saveResourceBundle(ResourceBundle resourceBundle) {
		getCurrentSession().flush();
		List<Map<String, Object>> rbKey = findResourceBundleByData(resourceBundle.getId().getResourceKey(),
				resourceBundle.getLanguage().getLanguageId());
		if (findResourceBundle(resourceBundle.getId()) == null && (rbKey == null || rbKey.isEmpty())) {
			getCurrentSession().persist(resourceBundle);
		} else {
			getCurrentSession().merge(resourceBundle);
		}
		sessionFactory.getCache().evictQueryRegion("resourceBundleDataQueryCache");

		String	cacheName	= "resourceBundleCache";
		String	cacheKey	= String.join(":", resourceBundle.getId().getResourceKey());
		cacheManager.invalidateDtoORScalarValues(cacheName, cacheKey);
	}
	
	public String findByKeyAndLanguageCode(String resourceBundleKey, String localeCode, String defaultLocaleCode,
			Integer isDeleted) {

		String cacheName = "resourceBundleCache";
		//String cacheKey = String.join(":", resourceBundleKey, localeCode, defaultLocaleCode, String.valueOf(isDeleted));
		String cacheKey = String.join(":", resourceBundleKey);
		return cacheManager.fetchJpaDto(cacheName, cacheKey, () -> {
			String hql = "SELECT COALESCE(rb.text, " + " (SELECT rbd.text FROM ResourceBundle AS rbd "
					+ "  INNER JOIN rbd.language AS lgd ON lgd.languageCode = :defaultLocaleCode AND lgd.isDeleted = :isDeleted "
					+ "  WHERE rbd.id.resourceKey = :resourceBundleKey)) " + "FROM Language AS lg "
					+ "LEFT OUTER JOIN lg.resourceBundles AS rb ON rb.id.resourceKey = :resourceBundleKey "
					+ "WHERE lg.languageCode = :localeCode AND lg.isDeleted = :isDeleted";
			try {
				return getCurrentSession().createQuery(hql, String.class)
						.setParameter("resourceBundleKey", resourceBundleKey).setParameter("localeCode", localeCode)
						.setParameter("defaultLocaleCode", defaultLocaleCode).setParameter("isDeleted", isDeleted)
						.setMaxResults(1).uniqueResult();
			} catch (NoResultException e) {
				return null;
			}

		}, resourceBundleTtl);
	}
}