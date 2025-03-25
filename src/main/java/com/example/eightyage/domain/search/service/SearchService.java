package com.example.eightyage.domain.search.service;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.entity.SearchLog;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import com.example.eightyage.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchLogRepository searchLogRepository;

    public void saveKeyword(String keyword){
        if(StringUtils.hasText(keyword)){
            searchLogRepository.save(SearchLog.of(keyword));
        }
    }

    public List<PopularKeywordDto> searchPoplarKeywordsWithinDays(int days) {
        if(days<0 || days >365){
            throw new BadRequestException("조회 기간은 1 ~ 365일 사이여야 합니다.");
        }
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return searchLogRepository.findPopularKeywords(since);
    }
}
