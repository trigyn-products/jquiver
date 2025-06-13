package com.trigyn.jws.webstarter.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Redisson-based query result caching utility for both
 * NamedParameterJdbcTemplate and JdbcTemplate.
 * 
 * - Uses Redisson's RMapCache to store query results with TTL. - Supports cache
 * read/write and invalidation. - Suitable for procedures or raw SQL.
 */
@Component
public class RedissonQueryCacheManagerUtil {

	// inside RedissonQueryCacheManager
	private static final Logger			logger				= LoggerFactory
			.getLogger(RedissonQueryCacheManagerUtil.class);

	@Autowired
	private NamedParameterJdbcTemplate	namedJdbcTemplate	= null;

	@Autowired
	private JdbcTemplate				jdbcTemplate		= null;

	@Autowired(required = false)
	private RedissonClient				redissonClient		= null;

	@Value("${jquiver.redis.cache.ttl:1800}") // 30 minutes
	private int								defaultTTLMinutes;

	@Value("${jquiver.redis.cache.enabled:false}")
	private boolean						isCacheEnabled;

	/**
	 * Get from Redisson cache or compute and store it.
	 *
	 * @param  <T>       DTO type
	 * @param  cacheName Redisson cache map name
	 * @param  cacheKey  Key to identify cached object
	 * @return           Cached or computed value
	 */
	public <T> T fetchJpaDto(String cacheName, String cacheKey, Supplier<T> dbFetcher, long ttlMinutes) {
		if (!isCacheEnabled && null == redissonClient)
			return dbFetcher.get(); // fallback if cache not available

		RMapCache<String, T>	cache		= redissonClient.getMapCache(cacheName);
		T						cachedValue	= cache.get(cacheKey);
		logger.debug("Value for key [" + cacheKey + "] = " + cachedValue);
		if (null != cachedValue) {
			logger.debug("Redisson DTO Cache HIT: [{}] key={}", cacheName, cacheKey);
			return cachedValue;
		}

		logger.debug("Redisson DTO Cache MISS: [{}] key={}", cacheName, cacheKey);
		T result = dbFetcher.get();
		if (null != result && isValidResult(result)) {
			cache.put(cacheKey, result, ttlMinutes, TimeUnit.MINUTES);
			logger.debug("Redisson DTO Cache PUT: [{}] key={} value={} TTL={}min", cacheName, cacheKey, result,
					ttlMinutes);
		} else {
			logger.warn("Redisson DTO Cache SKIPPED (null/invalid): [{}] key={} result={}", cacheName, cacheKey,
					result);
			cache.remove(cacheKey); // Ensure no blank entries linger
		}

		long ttl = cache.remainTimeToLive(cacheKey);
		logger.debug("Redisson DTO Cache TTL left: [{}] key={} TTL={}ms", cacheName, cacheKey, ttl);
		return result;
	}

	private <T> boolean isValidResult(T result) {
		if (result instanceof String) {
			return !((String) result).trim().isEmpty();
		}
		return result != null;
	}

	// Overloaded method with default TTL
	public <T> T fetchJpaDto(String cacheName, String cacheKey, Supplier<T> dbFetcher) {
		return fetchJpaDto(cacheName, cacheKey, dbFetcher, defaultTTLMinutes);
	}

	/**
	 * Fetches results using NamedParameterJdbcTemplate from cache or DB.
	 * 
	 * @param  cacheName     Redisson RMapCache name
	 * @param  sqlOrProcName SQL query or stored procedure name
	 * @param  paramMap      Named parameters
	 * @return               Cached or freshly queried result
	 */
	public List<Map<String, Object>> fetchNamedQuery(String cacheKey, String cacheName, String sqlOrProcName,
			Map<String, Object> paramMap) {

		RMapCache<String, List<Map<String, Object>>>	cache	= redissonClient.getMapCache(cacheName);

		List<Map<String, Object>>						cached	= cache.get(cacheKey);
		if (null != cached) {
			logger.debug("Redisson Cache HIT: [{}] key={}", cacheName, cacheKey);
			return cached;
		}

		logger.debug("Redisson Cache MISS: [{}] key={}", cacheName, cacheKey);
		List<Map<String, Object>> result = namedJdbcTemplate.queryForList(sqlOrProcName, paramMap);
		cache.put(cacheKey, result, defaultTTLMinutes, TimeUnit.MINUTES);
		return result;
	}

	/**
	 * Fetches results using JdbcTemplate from cache or DB.
	 * 
	 * @param  cacheName Redisson RMapCache name
	 * @param  sql       SQL query string
	 * @param  params    Positional parameters
	 * @return           Cached or freshly queried result
	 */
	public List<Map<String, Object>> fetchJdbcQuery(String cacheName, String sql, Object[] params) {

		String											cacheKey	= generatePositionalKey(sql, params);
		RMapCache<String, List<Map<String, Object>>>	cache		= redissonClient.getMapCache(cacheName);

		List<Map<String, Object>>						cached		= cache.get(cacheKey);
		if (null != cached) {
			logger.debug("Redisson Cache HIT: [{}] key={}", cacheName, cacheKey);
			return cached;
		}

		logger.debug("Redisson Cache MISS: [{}] key={}", cacheName, cacheKey);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, params);
		cache.put(cacheKey, result, defaultTTLMinutes, TimeUnit.MINUTES);
		return result;
	}

	public <T> List<T> fetchJpaDtoList(String cacheName, String key, Supplier<List<T>> dbFetcher, long ttlMinutes) {
		if (!isCacheEnabled && null == redissonClient)
			// Redisson caching is disabled — fetch from DB only
			return dbFetcher.get();

		RMapCache<String, List<T>>	mapCache	= redissonClient.getMapCache(cacheName);
		List<T>						result		= mapCache.get(key);

		if (null == result) {
			result = dbFetcher.get();
			if (null != result) {
				mapCache.put(key, result, ttlMinutes, TimeUnit.MINUTES);
			}
		}

		return result;
	}

	/**
	 * Invalidates a specific NamedParameterJdbcTemplate cache entry.
	 * 
	 * @param cacheName     Cache name
	 * @param sqlOrProcName SQL query or procedure
	 * @param paramMap      Named parameters
	 */
	public void invalidateNamedQuery(String cacheName, String sqlOrProcName, Map<String, Object> paramMap) {
		String cacheKey = generateNamedKey(sqlOrProcName, paramMap);
		redissonClient.getMapCache(cacheName).remove(cacheKey);
	}

	/**
	 * Invalidates a specific JdbcTemplate cache entry.
	 * 
	 * @param cacheName Cache name
	 * @param sql       SQL query
	 * @param params    Positional parameters
	 */
	public void invalidateJdbcQuery(String cacheName, String sql, Object[] params) {
		String cacheKey = generatePositionalKey(sql, params);
		redissonClient.getMapCache(cacheName).remove(cacheKey);
	}

	/**
	 * Generates a deterministic cache key based on SQL and named parameters.
	 * 
	 * @param  query    SQL or procedure name
	 * @param  paramMap Named parameters
	 * @return          A unique cache key string
	 */
	private String generateNamedKey(String query, Map<String, Object> paramMap) {
		StringBuilder sb = new StringBuilder(query).append(":");
		paramMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEach(e -> sb.append(e.getKey()).append("=").append(e.getValue()).append(";"));
		return sb.toString();
	}

	/**
	 * Generates a deterministic cache key based on SQL and positional parameters.
	 * 
	 * @param  query  SQL query
	 * @param  params Positional parameters
	 * @return        A unique cache key string
	 */
	private String generatePositionalKey(String query, Object[] params) {
		StringBuilder sb = new StringBuilder(query).append(":");
		if (null != params) {
			for (Object param : params) {
				sb.append(param).append(";");
			}
		}
		return sb.toString();
	}

	/**
	 * Invalidates cache entries in the specified RMapCache whose keys contain the
	 * given partial key string.
	 *
	 * @param cacheName  Name of the Redisson cache (RMapCache)
	 * @param keyPattern Partial key string to match (e.g. "targetLookupId=123;")
	 */
	public void invalidateByKeyPattern(String cacheName, String keyPattern) {
		RMapCache<String, ?> cache = redissonClient.getMapCache(cacheName);

		for (String key : cache.readAllKeySet()) {
			if (key.contains(keyPattern)) {
				cache.remove(key);
			}
		}
	}

	/**
	 * Invalidates a specific DTO entry from the Redisson cache.
	 * <p>
	 * This method removes the cached value associated with the given
	 * {@code cacheKey} from the specified {@code cacheName} (RMapCache). If
	 * Redisson caching is disabled via configuration or the Redisson client is not
	 * available, this method performs no action.
	 * </p>
	 *
	 * @param cacheName the name of the Redisson RMapCache where the DTO is stored
	 * @param cacheKey  the unique cache key for the DTO to be invalidated
	 *
	 */
	public void invalidateDtoORScalarValues(String cacheName, String cacheKey) {
		if (!isCacheEnabled || null == redissonClient)
			return;

		RMapCache<String, ?> cache = redissonClient.getMapCache(cacheName);
		cache.remove(cacheKey);
		logger.debug("Cache INVALIDATED: [{}] {}", cacheName, cacheKey);
	}
	/**
	 * Invalidates a specific region from the Redisson cache.
	 * <p>
	 * This method removes the cached value from the specified {@code cacheName}
	 * (RMapCache). If Redisson caching is disabled via configuration or the
	 * Redisson client is not available, this method performs no action.
	 * </p>
	 *
	 * @param cacheName the name of the Redisson RMapCache where the DTO is stored
	 *
	 */
	public void invalidateRegion(String cacheName) {
		if (!isCacheEnabled || redissonClient == null)
			return;
 
		RMapCache<String, ?> cache = redissonClient.getMapCache(cacheName);
		cache.clear();
		logger.debug("Cache REGION INVALIDATED: [{}]", cacheName);
	}
}
