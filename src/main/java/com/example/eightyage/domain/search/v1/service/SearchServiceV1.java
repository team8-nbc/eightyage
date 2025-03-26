package com.example.eightyage.domain.search.v1.service;

import com.example.eightyage.domain.search.entity.SearchLog;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SearchServiceV1 {

    private final SearchLogRepository searchLogRepository;

    public void saveSearchLog(String keyword){
        if(StringUtils.hasText(keyword)){
            searchLogRepository.save(SearchLog.of(keyword));
        }
    }
}
