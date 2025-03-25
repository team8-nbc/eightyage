package com.example.eightyage.domain.search.v1.service;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import com.example.eightyage.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularKeywordServiceV1 {

    private final SearchLogRepository searchLogRepository;

    // 캐시X 인기 검색어 조회
    @Transactional
    public List<PopularKeywordDto> searchPoplarKeywords(int days) {
        if (days < 0 || days > 365) {
            throw new BadRequestException("조회 기간은 1 ~ 365일 사이여야 합니다.");
        }
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return searchLogRepository.findPopularKeywords(since);
    }

}