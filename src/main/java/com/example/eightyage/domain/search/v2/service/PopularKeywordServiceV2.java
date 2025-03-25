package com.example.eightyage.domain.search.v2.service;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import com.example.eightyage.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularKeywordServiceV2 {

    private final SearchLogRepository searchLogRepository;

    //캐시O 인기 검색어 조회
    @Transactional
    @Cacheable(value = "popularKeywords", key = "#days")
    public List<PopularKeywordDto> searchPopularKeywordsV2(int days) {
        if (days < 1 || days > 365) {
            throw new BadRequestException("조회 일 수는 1~365 사이여야 합니다.");
        }
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return searchLogRepository.findPopularKeywords(since);
    }

}
