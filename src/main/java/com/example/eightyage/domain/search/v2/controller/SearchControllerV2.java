package com.example.eightyage.domain.search.v2.controller;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.v2.service.PopularKeywordServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchControllerV2 {

    private final PopularKeywordServiceV2 popularKeywordService;

    // 인기 검색어 조회 (캐시 O)
    @GetMapping("/api/v2/search/popular")
    public ResponseEntity<List<PopularKeywordDto>> searchPopularKeywordsV2(
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(popularKeywordService.searchPopularKeywordsV2(days));
    }

}
