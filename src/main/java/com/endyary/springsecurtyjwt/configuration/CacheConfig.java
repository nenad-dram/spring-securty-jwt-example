package com.endyary.springsecurtyjwt.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Cache configuration for UserDetails instances (applied on UserDetailsService.loadUserByUsername)
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String USER_CACHE = "userCache";

    @Bean
    public SimpleCacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(new ConcurrentMapCache(USER_CACHE)));
        return cacheManager;
    }
}
