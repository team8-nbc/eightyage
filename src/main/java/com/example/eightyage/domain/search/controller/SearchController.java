package com.example.eightyage.domain.search.controller;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/api/v1/search/popular")
    public ResponseEntity<List<PopularKeywordDto>> searchPopularKeywords(
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(searchService.searchPoplarKeywordsWithinDays(days));
    }

}
