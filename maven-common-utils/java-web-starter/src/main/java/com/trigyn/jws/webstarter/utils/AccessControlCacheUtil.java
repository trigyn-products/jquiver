package com.trigyn.jws.webstarter.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.trigyn.jws.webstarter.vo.AccessEntityType;

@Component
public class AccessControlCacheUtil {

	@Autowired
	private RedissonQueryCacheManagerUtil	cacheManager	= null;

	@Autowired(required = false)
	private RedissonClient					redissonClient	= null;

	@Value("${jquiver.redis.cache.enabled:false}")
	private boolean							isCacheEnabled;

	public Long hasAccess(String cacheName, AccessEntityType entityType, String entityId, List<String> roleNames,
			Supplier<Long> dbFetcher, long ttlMinutes) {
		List<String> sortedRoles = roleNames != null ? new ArrayList<>(roleNames) : Collections.emptyList();
		Collections.sort(sortedRoles);
		String key = buildCacheKey(entityType, entityId, sortedRoles);

		return cacheManager.fetchJpaDto(cacheName, key, dbFetcher, ttlMinutes); // TTL in minutes
	}

	public String buildCacheKey(AccessEntityType entityType, String entityId, List<String> sortedRoles) {
		String joinedRoles = String.join(",", sortedRoles);
		return "hasAccessTo" + entityType.name() + ":" + entityId + ":" + joinedRoles;
	}

	public void invalidateAccess(AccessEntityType entityType, String entityId) {
		String						prefix	= "hasAccessTo" + entityType.name() + ":" + entityId + ":";
		RMapCache<String, Object>	cache	= redissonClient.getMapCache(entityType.getCacheName());
		for (String key : cache.keySet()) {
			if (key.startsWith(prefix)) {
				cache.remove(key);
			}
		}
	}

	public void invalidateDashletAccessCache(String dashletId) {
		if (!isCacheEnabled || null == redissonClient)
			return;

		String					cacheName	= AccessEntityType.DASHLET.getCacheName();
		RMapCache<String, ?>	cache		= redissonClient.getMapCache(cacheName);

		// Pattern: hasAccessToDASHLET:<dashletId>:*
		String					keyPrefix	= "hasAccessToDASHLET:" + dashletId + ":";

		for (String key : cache.keySet()) {
			if (key.startsWith(keyPrefix)) {
				cache.remove(key);
				// logger.info("Invalidated cache key: {}", key);
			}
		}
	}

	public void invalidateDynamicRestAccessCache(String requestUri, String requestMethod) {
		if (!isCacheEnabled || redissonClient == null)
			return;

		String					cacheName	= AccessEntityType.DYNAMIC_REST.getCacheName();
		String					keyPrefix	= "hasAccessToDYNAMIC_REST:" + requestUri + ":" + requestMethod + ":";

		RMapCache<String, ?>	cache		= redissonClient.getMapCache(cacheName);
		for (String key : cache.keySet()) {
			if (key.startsWith(keyPrefix)) {
				cache.remove(key);
				// logger.info("Invalidated DynamicRest cache key: {}", key);
			}
		}
	}

	public void invalidateSiteLayoutAccessCache(String moduleUrl) {
		if (!isCacheEnabled || redissonClient == null)
			return;

		String					cacheName	= AccessEntityType.SITE_LAYOUT.getCacheName();
		String					keyPrefix	= "hasAccessToSITE_LAYOUT:" + moduleUrl + ":";

		RMapCache<String, ?>	cache		= redissonClient.getMapCache(cacheName);
		for (String key : cache.keySet()) {
			if (key.startsWith(keyPrefix)) {
				cache.remove(key);
				// logger.info("Invalidated SiteLayout cache key: {}", key);
			}
		}
	}

	public void invalidateEntityAccessCache(String moduleName, String entityId) {
		if (!isCacheEnabled || redissonClient == null)
			return;

		String					cacheName	= AccessEntityType.ENTITY.getCacheName();
		String					prefix		= "hasAccessToENTITY:" + moduleName + ":" + entityId + ":";

		RMapCache<String, ?>	cache		= redissonClient.getMapCache(cacheName);
		for (String key : cache.keySet()) {
			if (key.startsWith(prefix)) {
				cache.remove(key);
				// logger.info("Invalidated EntityAccess cache key: {}", key);
			}
		}
	}

}
