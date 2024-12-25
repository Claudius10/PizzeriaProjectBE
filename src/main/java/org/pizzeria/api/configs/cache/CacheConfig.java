package org.pizzeria.api.configs.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	/**
	 * Simple in-memory CacheManager.
	 *
	 * @return concurrent map cache manager
	 */
	@Bean
	CacheManager cacheManager() {
		return new ConcurrentMapCacheManager();
	}
}
