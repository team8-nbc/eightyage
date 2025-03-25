package com.example.eightyage.domain.search.v2.service;

import com.example.eightyage.domain.search.entity.SearchLog;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchLogRepository searchLogRepository;
    private final CacheManager cacheManager;

    // 검색 키워드를 로그에 저장
    @Transactional
    public void saveSearchLog(String keyword) {
        if (StringUtils.hasText(keyword)) {
            searchLogRepository.save(SearchLog.of(keyword));
        }
    }

    // 검색 시 키워드 카운트 증가
    public void increaseKeywordCount(String keyword) {
        if (!StringUtils.hasText(keyword)) return;

        Cache countCache = cacheManager.getCache("keywordCountMap");
        Cache keySetCache = cacheManager.getCache("keywordKeySet");

        if (countCache != null) {
            Long count = countCache.get(keyword, Long.class);
            count = (count == null) ? 1L : count + 1;
            countCache.put(keyword, count);
        }

        if (keySetCache != null) {
            Set<String> keywordSet = keySetCache.get("keywords", Set.class);
            if (keywordSet == null) {
                keywordSet = new HashSet<>();
            }
            keywordSet.add(keyword);
            keySetCache.put("keywords", keywordSet);
        }
    }

    @Transactional
    public void logAndCountKeyword(String keyword) {
        saveSearchLog(keyword);
        increaseKeywordCount(keyword);
    }

}
