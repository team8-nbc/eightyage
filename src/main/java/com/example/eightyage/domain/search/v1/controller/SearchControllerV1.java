package com.example.eightyage.domain.search.v1.controller;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.v1.service.PopularKeywordServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchControllerV1 {

    private final PopularKeywordServiceV1 popularKeywordService;

    // 인기 검색어 조회 (캐시 X)
    @GetMapping("/api/v1/search/popular")
    public ResponseEntity<List<PopularKeywordDto>> searchPopularKeywords(
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(popularKeywordService.searchPoplarKeywords(days));
    }
}
