package com.example.eightyage.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularKeywordDto {

    private String keyword;
    private Long count;

}
