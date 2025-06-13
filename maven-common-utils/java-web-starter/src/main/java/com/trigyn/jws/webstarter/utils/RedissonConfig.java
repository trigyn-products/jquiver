package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class RedissonConfig {

	private static final Logger	logger		= LoggerFactory.getLogger(RedissonConfig.class);

	@Value("${jquiver.redis.cache.enabled:false}")
	private boolean				isCacheEnabled;

	@Value("${jquiver.redis.config.path:redisson.yaml}")
	private String				redissonConfigPath;

	/**
	 * TTL and idle time in seconds (make sure your properties are in seconds, not
	 * ms)
	 */
	@Value("${jquiver.redis.cache.ttl:1600}") // 30 minutes
	private int					ttl;

	@Value("${jquiver.redis.cache.idle:1600}") // 30 minutes
	private int					idle;
	//
	// @Value("${jquiver.redis.cache.query.ttl:1800}") // 30 minutes
	// private int queryTtl;
	//
	// @Value("${jquiver.redis.cache.query.idle:1800}") // 30 minutes
	// private int queryIdle;

	private boolean				configPath	= true;

	@Bean(destroyMethod = "shutdown", name = "redissonClient")
	@ConditionalOnProperty(name = "jquiver.redis.cache.enabled", havingValue = "true", matchIfMissing = false)
	@SuppressWarnings("deprecation")
	public RedissonClient redissonClient() throws IOException {
		InputStream	inputStream;

		File		configFile	= new File(redissonConfigPath);
		if (configFile.exists() && configFile.isFile()) {
			// Load from external file system
			inputStream = new FileInputStream(configFile);
			logger.debug("Redisson config loaded from file: {}", configFile.getAbsolutePath());
		} else {
			// Load from classpath

			inputStream = new ClassPathResource("redisson.yaml").getInputStream();
			logger.debug("Redisson config loaded from classpath: redisson.yaml");
			configPath = false;
		}

		try (inputStream) {
			Config config = Config.fromYAML(inputStream);
			config.setCodec(new org.redisson.codec.Kryo5Codec()); // Optional: use your preferred codec
			RedissonClient redissonClient = Redisson.create(config);
			logger.debug("RedissonClient initialized successfully");
			return redissonClient;
		}
	}

	@Bean
	public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
			@Autowired(required = false) RedissonClient redissonClient) {
		return hibernateProperties -> {
			if (isCacheEnabled && redissonClient != null) {
				logger.debug("Configuring Hibernate second level cache with Redisson");

				hibernateProperties.put("hibernate.cache.use_second_level_cache", true);
				hibernateProperties.put("hibernate.cache.use_query_cache", true);
				hibernateProperties.put("hibernate.cache.region.factory_class",
						"org.redisson.hibernate.RedissonRegionFactory");
				hibernateProperties.put("hibernate.cache.redisson.instance", redissonClient);
				if (configPath)
					hibernateProperties.put("hibernate.cache.redisson.config", redissonConfigPath);

				// Query cache region with query TTL and idle
				configureCacheRegion(hibernateProperties, "org.hibernate.cache.internal.StandardQueryCache", "LRU", ttl,
						idle);

				List<String> cacheRegions = Arrays.asList("templateMasters", "applicationProperties", "resourceBundles",
						"gridDetailsRegion", "jwsDynamicRestDetailRegion", "jwsEntityRoleAssociationRegion",
						"jwsRoleRegion", "propertyMasterValueRegion", "resourceBundleDataQueryCache",
						"templateDtoRegion", "moduleDetailsCache", "targetTypeDetailsCache",
						"authorizedValidatorDAORegion", "requestTypeCache", "siteLayoutAccessCache",
						"autocomplete-access", "dashboard-access", "dashlet-access", "dynamicform-access",
						"dynamicRest-access", "entity-access", "gridUtil-access", "siteLayout-access",
						"template-access", "dashboard-name-cache", "form-name-cache", "file-bin-cache",
						"grid-name-cache", "manual-name-cache", "site-layout-module-cache");

				for (String region : cacheRegions) {
					// Entity cache regions with entity TTL and idle && // Additional cache regions
					configureCacheRegion(hibernateProperties, region, "LRU", ttl, idle);
				}

			} else {
				logger.debug("Caching disabled: Hibernate second level cache not configured");
				hibernateProperties.put("hibernate.cache.use_second_level_cache", false);
				hibernateProperties.put("hibernate.cache.use_query_cache", false);
				hibernateProperties.remove("hibernate.cache.region.factory_class");
			}
		};
	}

	private void configureCacheRegion(Map<String, Object> hibernateProperties, String regionName, String evictionPolicy,
			int timeToLiveSeconds, int maxIdleTimeSeconds) {

		String prefix = "hibernate.cache.redisson." + regionName + ".localcache.";

		hibernateProperties.put(prefix + "eviction_policy", evictionPolicy);
		hibernateProperties.put(prefix + "time_to_live", timeToLiveSeconds);
		hibernateProperties.put(prefix + "max_idle_time", maxIdleTimeSeconds);
		hibernateProperties.put(prefix + "size", 1000);
		hibernateProperties.put(prefix + "sync_strategy", "UPDATE");

		String remotePrefix = "hibernate.cache.redisson." + regionName + ".";
		hibernateProperties.put(remotePrefix + "time_to_live", timeToLiveSeconds);
		hibernateProperties.put(remotePrefix + "max_idle_time", maxIdleTimeSeconds);
	}

}
