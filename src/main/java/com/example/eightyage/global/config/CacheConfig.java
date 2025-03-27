package com.example.eightyage.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        // 키워드를 카운팅하는 캐시
        CaffeineCache keywordCountMap = new CaffeineCache(
                "keywordCountMap",
                Caffeine.newBuilder()
                        .maximumSize(10000)
                        .build()
        );

        // 인기 검색어를 조회하는 캐시
        CaffeineCache popularKeywords = new CaffeineCache(
                "popularKeywords",
                Caffeine.newBuilder()
                        .maximumSize(365) // days 값 기준으로 최대 365개
                        .expireAfterWrite(5, TimeUnit.MINUTES) // TTL 5분
                        .build()
        );

        // 현재 캐시에 저장된 키워드 목록
        CaffeineCache keywordKeySet = new CaffeineCache(
                "keywordKeySet",
                Caffeine.newBuilder()
                        .maximumSize(1)
                        .build()
        );

        cacheManager.setCaches(Arrays.asList(keywordCountMap, popularKeywords, keywordKeySet));
        return cacheManager;
    }

}
